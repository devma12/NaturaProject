package com.natura.web.server.persistence.database;

import com.natura.web.server.mapper.IdentificationMapper;
import com.natura.web.server.mapper.UserMapper;
import com.natura.web.server.model.Identification;
import com.natura.web.server.model.User;
import com.natura.web.server.persistence.database.entity.IdentificationEntity;
import com.natura.web.server.persistence.database.entity.UserEntity;
import com.natura.web.server.persistence.database.repository.IdentificationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Database identification provider should")
@ExtendWith(MockitoExtension.class)
class DatabaseIdentificationProviderTest {

    private static final Long ENTRY_ID = 1L;
    private static final Long SPECIES_ID = 1L;

    @InjectMocks
    DatabaseIdentificationProvider provider;

    @Mock
    IdentificationMapper mapper;

    @Mock
    UserMapper userMapper;

    @Mock
    IdentificationRepository repository;

    @Test
    @DisplayName("save identification.")
    void save() {
        // Given
        IdentificationEntity entity = new IdentificationEntity();
        Identification identification = new Identification();
        Identification saved = new Identification();
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.map(identification)).thenReturn(entity);
        when(mapper.map(entity)).thenReturn(saved);

        // When
        Identification result = provider.save(identification);

        // Then
        verify(repository).save(entity);
        assertThat(result).isEqualTo(saved);
    }

    @Test
    @DisplayName("return identification with given entry id and species id if exists.")
    void getIdentificationByEntryIdAndSpeciesId() {
        // Given
        Optional<IdentificationEntity> entity = Optional.of(new IdentificationEntity());
        Identification identification = new Identification();
        when(repository.findByIdEntryIdAndIdSpeciesId(ENTRY_ID, SPECIES_ID)).thenReturn(entity);
        when(mapper.map(entity)).thenReturn(Optional.of(identification));

        // When
        Optional<Identification> result = provider.getIdentificationByEntryIdAndSpeciesId(ENTRY_ID, SPECIES_ID);

        // Then
        assertThat(result).isPresent().contains(identification);
    }

    @Test
    @DisplayName("return empty if no identification exists with given entry id and species id.")
    void getIdentificationByEntryIdAndSpeciesId_null() {
        // Given
        when(repository.findByIdEntryIdAndIdSpeciesId(ENTRY_ID, SPECIES_ID)).thenReturn(Optional.empty());

        // When
        Optional<Identification> result = provider.getIdentificationByEntryIdAndSpeciesId(ENTRY_ID, SPECIES_ID);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("return identifications suggested by given user.")
    void getIdentificationsBySuggestedByUser() {
        // Given
        List<IdentificationEntity> entities = List.of(new IdentificationEntity());
        List<Identification> identifications = List.of(new Identification());
        User user = new User();
        UserEntity userEntity = new UserEntity();
        when(repository.findBySuggestedBy(userEntity)).thenReturn(entities);
        when(userMapper.map(user)).thenReturn(userEntity);
        when(mapper.map(entities)).thenReturn(identifications);

        // When
        List<Identification> result = provider.getIdentificationsBySuggestedByUser(user);

        // Then
        assertThat(result).isEqualTo(identifications);
    }

    @Test
    @DisplayName("return identifications gor given entry id.")
    void getIdentificationsByEntryId() {
        // Given
        List<IdentificationEntity> entities = List.of(new IdentificationEntity());
        List<Identification> identifications = List.of(new Identification());
        when(repository.findByIdEntryId(ENTRY_ID)).thenReturn(entities);
        when(mapper.map(entities)).thenReturn(identifications);

        // When
        List<Identification> result = provider.getIdentificationsByEntryId(ENTRY_ID);

        // Then
        assertThat(result).isEqualTo(identifications);
    }
}