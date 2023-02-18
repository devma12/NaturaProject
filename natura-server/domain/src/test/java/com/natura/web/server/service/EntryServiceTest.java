package com.natura.web.server.service;

import com.natura.web.server.exception.DataNotFoundException;
import com.natura.web.server.exception.InvalidDataException.InvalidFileException;
import com.natura.web.server.model.Entry;
import com.natura.web.server.model.Identification;
import com.natura.web.server.model.Image;
import com.natura.web.server.model.Species;
import com.natura.web.server.model.User;
import com.natura.web.server.provider.EntryProvider;
import com.natura.web.server.provider.IdentificationProvider;
import com.natura.web.server.provider.SpeciesProvider;
import com.natura.web.server.provider.UserProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Entry service should")
@ExtendWith(MockitoExtension.class)
class EntryServiceTest {

    static class EntryTest extends Entry {
        public EntryTest() {
            super();
        }
    }

    private static final Long USER_ID = 1L;
    private static final Long SPECIES_ID = 1L;

    @InjectMocks
    EntryService entryService;

    @Mock
    UserProvider userProvider;

    @Mock
    EntryProvider entryProvider;

    @Mock
    SpeciesProvider speciesProvider;

    @Mock
    IdentificationProvider identificationProvider;

    @Mock
    ImageService imageService;

    @Test
    @DisplayName("create new entry and suggested identification when species id is given.")
    void create_should_save_new_entry_and_identification() throws DataNotFoundException, InvalidFileException {
        // Given
        String imageName = "mocked";
        String contentType = "contentType";
        Entry newEntry = new EntryTest();
        InputStream is = new ByteArrayInputStream(new byte[10]);
        User user = User.builder().id(USER_ID).username("user").build();
        Species species = Species.builder().id(SPECIES_ID).commonName("species").build();
        when(userProvider.getUserById(USER_ID)).thenReturn(Optional.of(user));
        when(imageService.upload(imageName, contentType, is))
                .thenReturn(Image.builder().name(imageName).build());
        when(speciesProvider.getSpeciesById(SPECIES_ID)).thenReturn(Optional.of(species));
        when(entryProvider.save(newEntry)).thenReturn(newEntry);

        // When
        Entry created = entryService.createEntryAndSuggestedIdentification(newEntry, imageName, contentType, is, USER_ID, SPECIES_ID);

        // Then
        ArgumentCaptor<Identification> identificationArgumentCaptor = ArgumentCaptor.forClass(Identification.class);
        verify(entryProvider).save(newEntry);
        verify(identificationProvider).save(identificationArgumentCaptor.capture());
        Identification identification = identificationArgumentCaptor.getValue();
        assertThat(identification.getEntry()).isEqualTo(newEntry);
        assertThat(identification.getSpecies()).isEqualTo(species);
        assertThat(identification.getSuggestedBy()).isEqualTo(user);
        assertThat(created).isEqualTo(newEntry);
    }

    @Test
    @DisplayName("create new entry but no suggested identification when species id is not given")
    void create_should_save_new_entry_but_not_identification() throws DataNotFoundException, InvalidFileException {
        // Given
        String imageName = "mocked";
        String contentType = "contentType";
        Entry newEntry = new EntryTest();
        InputStream is = new ByteArrayInputStream(new byte[10]);
        User user = User.builder().id(USER_ID).username("user").build();
        when(userProvider.getUserById(USER_ID)).thenReturn(Optional.of(user));
        when(imageService.upload(imageName, contentType, is)).thenReturn(Image.builder().name(imageName).build());
        when(speciesProvider.getSpeciesById(any())).thenReturn(Optional.empty());
        when(entryProvider.save(newEntry)).thenReturn(newEntry);

        // When
        Entry created = entryService.createEntryAndSuggestedIdentification(newEntry, imageName, contentType, is, USER_ID, null);

        // Then
        verify(entryProvider).save(newEntry);
        verify(identificationProvider, never()).save(any());
        assertThat(created).isEqualTo(newEntry);
    }

    @Test
    @DisplayName("raise DataNotFoundException when no user is found with given id during entry creation")
    void create_should_raise_DataNotFoundException_when_no_user_is_found() {
        // Given
        Entry newEntry = new EntryTest();
        InputStream is = new ByteArrayInputStream(new byte[10]);
        when(userProvider.getUserById(USER_ID)).thenReturn(Optional.empty());

        // When
        Throwable thrown = catchThrowable(() -> entryService.createEntryAndSuggestedIdentification(newEntry, "mocked", "contentType", is, USER_ID, null));

        // Then
        assertThat(thrown).isInstanceOf(DataNotFoundException.class);
    }

}