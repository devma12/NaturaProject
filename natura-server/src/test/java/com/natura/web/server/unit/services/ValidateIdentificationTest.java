package com.natura.web.server.unit.services;

import com.natura.web.server.entities.Entry;
import com.natura.web.server.entities.Flower;
import com.natura.web.server.entities.Identification;
import com.natura.web.server.entities.Insect;
import com.natura.web.server.entities.Species;
import com.natura.web.server.entities.User;
import com.natura.web.server.exceptions.DataNotFoundException;
import com.natura.web.server.exceptions.InvalidDataException;
import com.natura.web.server.exceptions.UserAccountException;
import com.natura.web.server.repository.EntryRepository;
import com.natura.web.server.repository.IdentificationRepository;
import com.natura.web.server.repository.SpeciesRepository;
import com.natura.web.server.repository.UserRepository;
import com.natura.web.server.services.IdentificationService;
import java.util.Date;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ValidateIdentificationTest {

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
    fSpecies.setType(Species.Type.FLOWER);
    Mockito.lenient().when(speciesRepository.findById(1L)).thenReturn(Optional.of(fSpecies));
    Species iSpecies = new Species();
    iSpecies.setType(Species.Type.INSECT);
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
    species.setType(Species.Type.FLOWER);
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
    species.setType(Species.Type.FLOWER);
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
  void validateFlowerIdentificationByFlowerValidator()
      throws DataNotFoundException, InvalidDataException, UserAccountException.ValidationPermissionException {
    Identification identification = identificationService.validate(1L, 1L, 2L);
    Assertions.assertNotNull(identification, "Validated identification should not be null for valid validator and entry & species ids.");
  }

  @Test
  void validateInsectIdentificationByInsectValidator()
      throws DataNotFoundException, InvalidDataException, UserAccountException.ValidationPermissionException {
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
