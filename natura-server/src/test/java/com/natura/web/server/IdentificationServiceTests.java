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
        Mockito.lenient().when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        User flowerValidator = new User("flower_validator");
        flowerValidator.setFlowerValidator(true);
        Mockito.lenient().when(userRepository.findById(2L)).thenReturn(Optional.of(flowerValidator));
        User insectValidator = new User("insect_validator");
        insectValidator.setInsectValidator(true);
        Mockito.lenient().when(userRepository.findById(3L)).thenReturn(Optional.of(insectValidator));

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
    void createIdentificationForSpecificEntryAndSpeciesByKnownUser() throws DataNotFoundException {
        Identification identification = identificationService.identify(1L, 1L, 1L);
        Assertions.assertNotNull(identification, "Created identification should not be null for known entry, species and user ids.");
    }

    @Test
    void cannotValidateIdentificationWithoutUser() {
        Assertions.assertThrows(DataNotFoundException.class, () -> {
            identificationService.validate(1L, 1L, -1L);
        });
    }

    @Test
    void cannotValidateNonExistingIdentification() {
        Assertions.assertThrows(DataNotFoundException.class, () -> {
            identificationService.validate(2L, 1L, 1L);
        });
    }

    @Test
    void userCannotValidateFlowerIdentificationWithoutBeingFlowerValidator() {
        Assertions.assertThrows(UserAccountException.ValidationPermissionException.class, () -> {
            identificationService.validate(1L, 1L, 1L);
        });
    }

    @Test
    void userCannotValidateInsectIdentificationWithoutBeingInsectValidator() {
        Assertions.assertThrows(UserAccountException.ValidationPermissionException.class, () -> {
            identificationService.validate(2L, 2L, 2L);
        });
    }

    @Test
    void cannotValidateIdentificationForAlreadyIdentifiedEntry() {
        User user = new User("user");
        Mockito.lenient().when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Species species = new Species();
        species.setType(Species.Type.Flower);
        Mockito.lenient().when(speciesRepository.findById(1L)).thenReturn(Optional.of(species));

        Entry entry = new Flower();
        entry.setValidated(true);
        Mockito.lenient().when(entryRepository.findById(1L)).thenReturn(Optional.of(entry));

        Identification identification = new Identification(entry, species, user, new Date());
        Mockito.lenient().when(identificationRepository.findByIdEntryIdAndIdSpeciesId(1L, 1L)).thenReturn(identification);

        Assertions.assertThrows(InvalidDataException.AlreadyValidatedDataException.class, () -> {
            identificationService.validate(1L, 1L, 2L);
        });
    }

    @Test
    void userCannotValidateItsOwnSuggestedIdentification() {
        User user = new User("user");
        user.setFlowerValidator(true);
        Mockito.lenient().when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Species species = new Species();
        species.setType(Species.Type.Flower);
        Mockito.lenient().when(speciesRepository.findById(1L)).thenReturn(Optional.of(species));

        Entry entry = new Flower();
        Mockito.lenient().when(entryRepository.findById(1L)).thenReturn(Optional.of(entry));

        Identification identification = new Identification(entry, species, user, new Date());
        Mockito.lenient().when(identificationRepository.findByIdEntryIdAndIdSpeciesId(1L, 1L)).thenReturn(identification);

        Assertions.assertThrows(UserAccountException.ValidationPermissionException.class, () -> {
            identificationService.validate(1L, 1L, 1L);
        });
    }

    @Test
    void validateFlowerIdentificationByFlowerValidator() throws DataNotFoundException, InvalidDataException, UserAccountException.ValidationPermissionException {
        Identification identification = identificationService.validate(1L, 1L, 2L);
        Assertions.assertNotNull(identification, "Validated identification should not be null for valid validator and entry & species ids.");
    }

    @Test
    void validateInsectIdentificationByInsectValidator() throws DataNotFoundException, InvalidDataException, UserAccountException.ValidationPermissionException {
        Identification identification = identificationService.validate(2L, 2L, 3L);
        Assertions.assertNotNull(identification, "Validated identification should not be null for valid validator and entry & species ids.");
    }

    @Test
    void cannotGiveValidatorRightsToNullUser() {
        Assertions.assertThrows(DataNotFoundException.class, () -> {
            identificationService.giveValidatorRights(null);
        });
    }

}
