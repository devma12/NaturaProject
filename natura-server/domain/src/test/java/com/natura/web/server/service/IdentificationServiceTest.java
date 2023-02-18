package com.natura.web.server.service;

import com.natura.web.server.exception.DataNotFoundException;
import com.natura.web.server.exception.InvalidDataException;
import com.natura.web.server.exception.UserAccountException;
import com.natura.web.server.model.Entry;
import com.natura.web.server.model.Flower;
import com.natura.web.server.model.Identification;
import com.natura.web.server.model.Insect;
import com.natura.web.server.model.Species;
import com.natura.web.server.model.SpeciesType;
import com.natura.web.server.model.User;
import com.natura.web.server.provider.EntryProvider;
import com.natura.web.server.provider.IdentificationProvider;
import com.natura.web.server.provider.SpeciesProvider;
import com.natura.web.server.provider.UserProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.mockito.Mockito.when;

@DisplayName("Identification service should")
@ExtendWith(MockitoExtension.class)
public class IdentificationServiceTest {

    @InjectMocks
    IdentificationService identificationService;

    @Mock
    IdentificationProvider identificationProvider;

    @Mock
    UserProvider userProvider;

    @Mock
    SpeciesProvider speciesProvider;

    @Mock
    EntryProvider entryProvider;

    private Species flowerSpecies;

    private Species insectSpecies;

    Entry flower;

    Entry insect;

    User user;

    @BeforeEach
    void init() {

        Mockito.lenient().when(identificationProvider.save(Mockito.any())).then(AdditionalAnswers.returnsFirstArg());

        user = new User("test");
        Mockito.lenient().when(userProvider.getUserById(1L)).thenReturn(Optional.of(user));
        User flowerValidator = new User("flower_validator");
        flowerValidator.setFlowerValidator(true);
        Mockito.lenient().when(userProvider.getUserById(2L)).thenReturn(Optional.of(flowerValidator));
        User insectValidator = new User("insect_validator");
        insectValidator.setInsectValidator(true);
        Mockito.lenient().when(userProvider.getUserById(3L)).thenReturn(Optional.of(insectValidator));

        flowerSpecies = new Species();
        flowerSpecies.setType(SpeciesType.Flower);
        Mockito.lenient().when(speciesProvider.getSpeciesById(1L)).thenReturn(Optional.of(flowerSpecies));
        insectSpecies = new Species();
        insectSpecies.setType(SpeciesType.Insect);
        Mockito.lenient().when(speciesProvider.getSpeciesById(2L)).thenReturn(Optional.of(insectSpecies));

        flower = new Flower();
        Mockito.lenient().when(entryProvider.getEntryById(1L)).thenReturn(Optional.of(flower));
        insect = new Insect();
        Mockito.lenient().when(entryProvider.getEntryById(2L)).thenReturn(Optional.of(insect));

        Identification identification2 = new Identification(insect, insectSpecies, user, new Date());
        Mockito.lenient().when(identificationProvider.getIdentificationByEntryIdAndSpeciesId(2L, 2L)).thenReturn(Optional.of(identification2));

    }

    @Test
    void cannotCreateIdentificationWithoutEntry() {
        Assertions.assertThrows(DataNotFoundException.class, () -> identificationService.identify(-1L, 1L, 1L));
    }

    @Test
    void cannotCreateIdentificationWithoutSpecies() {
        Assertions.assertThrows(DataNotFoundException.class, () -> identificationService.identify(1L, -1L, 1L));
    }

    @Test
    void cannotCreateIdentificationWithoutUser() {
        Assertions.assertThrows(DataNotFoundException.class, () -> identificationService.identify(1L, 1L, -1L));
    }

    @Test
    void cannotCreateIdentificationThatAlreadyExists() {
        Assertions.assertThrows(InvalidDataException.DuplicateDataException.class, () -> identificationService.identify(2L, 2L, 1L));
    }

    @Test
    void cannotCreateIdentificationForInconsistentEntryAndSpecies() {
        Assertions.assertThrows(InvalidDataException.class, () -> identificationService.identify(1L, 2L, 1L));
    }

    @Test
    void cannotCreateIdentificationForInconsistentEntryAndSpecies2() {
        Assertions.assertThrows(InvalidDataException.class, () -> identificationService.identify(2L, 1L, 1L));
    }

    @Test
    void createIdentificationForConsistentEntryAndSpeciesByKnownUser() throws DataNotFoundException, InvalidDataException {
        Identification identification = identificationService.identify(1L, 1L, 1L);
        Assertions.assertNotNull(identification, "Created identification should not be null for known entry, species and user ids.");
    }

    @Test
    void cannotLikeIdentificationWithoutUser() {
        Assertions.assertThrows(DataNotFoundException.class, () -> identificationService.like(1L, 1L, -1L));
    }

    @Test
    void cannotLikeIdentificationWithInvalidEntryId() {
        Assertions.assertThrows(DataNotFoundException.class, () -> identificationService.like(-1L, 1L, 1L));
    }

    @Test
    void cannotLikeIdentificationWithInvalidSpeciesId() {
        Assertions.assertThrows(DataNotFoundException.class, () -> identificationService.like(1L, -1L, 1L));
    }

    @Test
    void likeIdentification() throws DataNotFoundException {
        User user = new User("liker");
        Mockito.lenient().when(userProvider.getUserById(2L)).thenReturn(Optional.of(user));

        when(identificationProvider.save(Mockito.any())).then(AdditionalAnswers.returnsFirstArg());

        Identification identification = new Identification(flower, flowerSpecies, user, new Date());
        when(identificationProvider.getIdentificationByEntryIdAndSpeciesId(1L, 1L)).thenReturn(Optional.of(identification));

        Identification result = identificationService.like(1L, 1L, 2L);
        Assertions.assertTrue(result.getLikes() != null
                && result.getLikes().size() == 1);
    }

    @Test
    void dislikeIdentification() throws DataNotFoundException {
        User user = new User("liker");
        user.setId(2L);
        Mockito.lenient().when(userProvider.getUserById(2L)).thenReturn(Optional.of(user));

        Identification identification = new Identification(flower, flowerSpecies, user, new Date());
        when(identificationProvider.getIdentificationByEntryIdAndSpeciesId(1L, 1L)).thenReturn(Optional.of(identification));
        identification.getLikes().add(user);
        Assertions.assertTrue(identification.getLikes() != null
                && identification.getLikes().size() == 1);

        when(identificationProvider.save(Mockito.any())).then(AdditionalAnswers.returnsFirstArg());

        Identification disliked = identificationService.like(1L, 1L, 2L);
        Assertions.assertTrue(disliked.getLikes() != null && disliked.getLikes().size() == 0);
    }

    @Test
    void cannotValidateIdentificationWithoutUser() {
        Assertions.assertThrows(DataNotFoundException.class, () -> identificationService.validate(1L, 1L, -1L));
    }

    @Test
    void cannotValidateNonExistingIdentification() {
        Assertions.assertThrows(DataNotFoundException.class, () -> identificationService.validate(2L, 1L, 1L));
    }

    @Test
    void userCannotValidateFlowerIdentificationWithoutBeingFlowerValidator() {
        Identification identification = new Identification(flower, flowerSpecies, user, new Date());
        when(identificationProvider.getIdentificationByEntryIdAndSpeciesId(1L, 1L)).thenReturn(Optional.of(identification));

        Assertions.assertThrows(UserAccountException.ValidationPermissionException.class, () -> identificationService.validate(1L, 1L, 1L));
    }

    @Test
    void userCannotValidateInsectIdentificationWithoutBeingInsectValidator() {
        Assertions.assertThrows(UserAccountException.ValidationPermissionException.class, () -> identificationService.validate(2L, 2L, 2L));
    }

    @Test
    void cannotValidateIdentificationForAlreadyIdentifiedEntry() {
        User user = new User("user");
        Mockito.lenient().when(userProvider.getUserById(1L)).thenReturn(Optional.of(user));

        Species species = new Species();
        species.setType(SpeciesType.Flower);
        Mockito.lenient().when(speciesProvider.getSpeciesById(1L)).thenReturn(Optional.of(species));

        Entry entry = new Flower();
        entry.setValidated(true);
        Mockito.lenient().when(entryProvider.getEntryById(1L)).thenReturn(Optional.of(entry));

        Identification identification = new Identification(entry, species, user, new Date());
        Mockito.lenient().when(identificationProvider.getIdentificationByEntryIdAndSpeciesId(1L, 1L)).thenReturn(Optional.of(identification));

        Assertions.assertThrows(InvalidDataException.AlreadyValidatedDataException.class, () -> identificationService.validate(1L, 1L, 2L));
    }

    @Test
    void userCannotValidateItsOwnSuggestedIdentification() {
        User user = new User("user");
        user.setFlowerValidator(true);
        Mockito.lenient().when(userProvider.getUserById(1L)).thenReturn(Optional.of(user));

        Species species = new Species();
        species.setType(SpeciesType.Flower);
        Mockito.lenient().when(speciesProvider.getSpeciesById(1L)).thenReturn(Optional.of(species));

        Entry entry = new Flower();
        Mockito.lenient().when(entryProvider.getEntryById(1L)).thenReturn(Optional.of(entry));

        Identification identification = new Identification(entry, species, user, new Date());
        Mockito.lenient().when(identificationProvider.getIdentificationByEntryIdAndSpeciesId(1L, 1L)).thenReturn(Optional.of(identification));

        Assertions.assertThrows(UserAccountException.ValidationPermissionException.class, () -> identificationService.validate(1L, 1L, 1L));
    }

    @Test
    void validateFlowerIdentificationByFlowerValidator() throws DataNotFoundException, InvalidDataException, UserAccountException.ValidationPermissionException {
        Identification identification = new Identification(flower, flowerSpecies, user, new Date());
        when(identificationProvider.getIdentificationByEntryIdAndSpeciesId(1L, 1L)).thenReturn(Optional.of(identification));

        Identification result = identificationService.validate(1L, 1L, 2L);
        Assertions.assertNotNull(result, "Validated identification should not be null for valid validator and entry & species ids.");
    }

    @Test
    void validateInsectIdentificationByInsectValidator() throws DataNotFoundException, InvalidDataException, UserAccountException.ValidationPermissionException {
        Identification identification = identificationService.validate(2L, 2L, 3L);
        Assertions.assertNotNull(identification, "Validated identification should not be null for valid validator and entry & species ids.");
    }

    @Test
    void cannotGiveValidatorRightsToNullUser() {
        Assertions.assertThrows(DataNotFoundException.class, () -> identificationService.giveValidatorRights(null));
    }

}
