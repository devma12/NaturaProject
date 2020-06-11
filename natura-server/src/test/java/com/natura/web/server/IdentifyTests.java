package com.natura.web.server;

import com.natura.web.server.entities.*;
import com.natura.web.server.exceptions.DataNotFoundException;
import com.natura.web.server.exceptions.InvalidDataException;
import com.natura.web.server.exceptions.UserAccountException;
import com.natura.web.server.repo.EntryRepository;
import com.natura.web.server.repo.IdentificationRepository;
import com.natura.web.server.repo.SpeciesRepository;
import com.natura.web.server.repo.UserRepository;
import com.natura.web.server.services.IdentificationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
public class IdentifyTests {

    @Autowired
    IdentificationService identificationService;

    @MockBean
    IdentificationRepository identificationRepository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    SpeciesRepository speciesRepository;

    @MockBean
    EntryRepository entryRepository;

    @BeforeEach
    void init() {

        Mockito.lenient().when(identificationRepository.save(Mockito.any())).then(AdditionalAnswers.returnsFirstArg());

        User user = new User("test");
        Mockito.lenient().when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Species fSpecies = new Species();
        fSpecies.setType(Species.Type.Flower);
        Mockito.lenient().when(speciesRepository.findById(1L)).thenReturn(Optional.of(fSpecies));
        Species iSpecies = new Species();
        iSpecies.setType(Species.Type.Insect);
        Mockito.lenient().when(speciesRepository.findById(2L)).thenReturn(Optional.of(iSpecies));

        Entry flower = new Flower();
        Mockito.lenient().when(entryRepository.findById(1L)).thenReturn(Optional.of(flower));
        Entry insect = new Insect();
        Mockito.lenient().when(entryRepository.findById(2L)).thenReturn(Optional.of(insect));

        Identification identification2 = new Identification(insect, iSpecies, user, new Date());
        Mockito.lenient().when(identificationRepository.findByIdEntryIdAndIdSpeciesId(2L, 2L)).thenReturn(identification2);

    }

    @Test
    void cannotCreateIdentificationWithoutEntry() {
        Assertions.assertThrows(DataNotFoundException.class, () -> {
            identificationService.identify(-1L, 1L, 1L);
        });
    }

    @Test
    void cannotCreateIdentificationWithoutSpecies() {
        Assertions.assertThrows(DataNotFoundException.class, () -> {
            identificationService.identify(1L, -1L, 1L);
        });
    }

    @Test
    void cannotCreateIdentificationWithoutUser() {
        Assertions.assertThrows(DataNotFoundException.class, () -> {
            identificationService.identify(1L, 1L, -1L);
        });
    }

    @Test
    void cannotCreateIdentificationThatAlreadyExists() {
        Assertions.assertThrows(InvalidDataException.DuplicateDataException.class, () -> {
            identificationService.identify(2L, 2L, 1L);
        });
    }

    @Test
    void cannotCreateIdentificationForInconsistentEntryAndSpecies() {
        Assertions.assertThrows(InvalidDataException.class, () -> {
            identificationService.identify(1L, 2L, 1L);
        });
    }

    @Test
    void cannotCreateIdentificationForInconsistentEntryAndSpecies2() {
        Assertions.assertThrows(InvalidDataException.class, () -> {
            identificationService.identify(2L, 1L, 1L);
        });
    }

    @Test
    void createIdentificationForConsistentEntryAndSpeciesByKnownUser() throws DataNotFoundException, InvalidDataException {
        Identification identification = identificationService.identify(1L, 1L, 1L);
        Assertions.assertNotNull(identification, "Created identification should not be null for known entry, species and user ids.");
    }

}
