package com.natura.web.server.unit.services;

import com.natura.web.server.entities.*;
import com.natura.web.server.exceptions.DataNotFoundException;
import com.natura.web.server.repository.EntryRepository;
import com.natura.web.server.repository.IdentificationRepository;
import com.natura.web.server.repository.SpeciesRepository;
import com.natura.web.server.repository.UserRepository;
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
class LikeIdentificationTest {

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

        Identification identification = new Identification(flower, fSpecies, user, new Date());
        Mockito.lenient().when(identificationRepository.findByIdEntryIdAndIdSpeciesId(1L, 1L)).thenReturn(identification);

        Identification identification2 = new Identification(insect, iSpecies, user, new Date());
        Mockito.lenient().when(identificationRepository.findByIdEntryIdAndIdSpeciesId(2L, 2L)).thenReturn(identification2);
    }

    @Test
    void cannotLikeIdentificationWithoutUser() {
        Assertions.assertThrows(DataNotFoundException.class, () -> {
            identificationService.like(1L, 1L, -1L);
        });
    }

    @Test
    void cannotLikeIdentificationWithInvalidEntryId() {
        Assertions.assertThrows(DataNotFoundException.class, () -> {
            identificationService.like(-1L, 1L, 1L);
        });
    }

    @Test
    void cannotLikeIdentificationWithInvalidSpeciesId() {
        Assertions.assertThrows(DataNotFoundException.class, () -> {
            identificationService.like(1L, -1L, 1L);
        });
    }

    @Test
    void likeIdentification() throws DataNotFoundException {
        User user = new User("liker");
        Mockito.lenient().when(userRepository.findById(2L)).thenReturn(Optional.of(user));

        Mockito.when(identificationRepository.save(Mockito.any())).then(AdditionalAnswers.returnsFirstArg());

        Identification identification = identificationService.like(1L, 1L, 2L);
        Assertions.assertTrue(identification.getLikes() != null
            && identification.getLikes().size() == 1);
    }

    @Test
    void dislikeIdentification() throws DataNotFoundException {
        User user = new User("liker");
        user.setId(2L);
        Mockito.lenient().when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        Identification identification = identificationRepository.findByIdEntryIdAndIdSpeciesId(1L, 1L);
        identification.getLikes().add(user);
        Assertions.assertTrue(identification.getLikes() != null
            && identification.getLikes().size() == 1);
        Mockito.lenient().when(identificationRepository.findByIdEntryIdAndIdSpeciesId(1L, 1L)).thenReturn(identification);

        Mockito.when(identificationRepository.save(Mockito.any())).then(AdditionalAnswers.returnsFirstArg());

        Identification disliked = identificationService.like(1L, 1L, 2L);
        Assertions.assertTrue(disliked.getLikes() != null && disliked.getLikes().size() == 0);
    }
}