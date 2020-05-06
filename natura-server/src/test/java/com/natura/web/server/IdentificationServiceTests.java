package com.natura.web.server;

import com.natura.web.server.entities.*;
import com.natura.web.server.exceptions.DataNotFoundException;
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

import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
public class IdentificationServiceTests {

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
        Species species = new Species();
        Entry entry = new Flower();
        Mockito.lenient().when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.lenient().when(speciesRepository.findById(1L)).thenReturn(Optional.of(species));
        Mockito.lenient().when(entryRepository.findById(1L)).thenReturn(Optional.of(entry));

    }

    @Test
    void cannotCreateIdentificationWithoutEntry() {
        Assertions.assertThrows(DataNotFoundException.class, () -> {
            identificationService.identify(2L, 1L, 1L);
        });
    }

    @Test
    void cannotCreateIdentificationWithoutSpecies() {
        Assertions.assertThrows(DataNotFoundException.class, () -> {
            identificationService.identify(1L, 2L, 1L);
        });
    }

    @Test
    void cannotCreateIdentificationWithoutUser() {
        Assertions.assertThrows(DataNotFoundException.class, () -> {
            identificationService.identify(1L, 1L, 2L);
        });
    }

    @Test
    void createIdentificationForSpecificEntryAndSpeciesByKnownUser() throws DataNotFoundException {
        Identification identification = identificationService.identify(1L, 1L, 1L);
        Assertions.assertNotNull(identification, "Create identification should not be null for known entry, species and user ids.");
    }
}
