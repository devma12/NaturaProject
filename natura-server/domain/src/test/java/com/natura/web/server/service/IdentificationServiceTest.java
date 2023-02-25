package com.natura.web.server.service;

import com.natura.web.server.exception.DataNotFoundException;
import com.natura.web.server.exception.InvalidDataException;
import com.natura.web.server.exception.UserAccountException;
import com.natura.web.server.model.Comment;
import com.natura.web.server.model.Entry;
import com.natura.web.server.model.Flower;
import com.natura.web.server.model.Identification;
import com.natura.web.server.model.Insect;
import com.natura.web.server.model.Species;
import com.natura.web.server.model.SpeciesType;
import com.natura.web.server.model.User;
import com.natura.web.server.provider.CommentProvider;
import com.natura.web.server.provider.EntryProvider;
import com.natura.web.server.provider.IdentificationProvider;
import com.natura.web.server.provider.SpeciesProvider;
import com.natura.web.server.provider.UserProvider;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Identification service should")
@ExtendWith(MockitoExtension.class)
class IdentificationServiceTest {

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

    @Mock
    CommentProvider commentProvider;

    private Species flowerSpecies;

    private Species insectSpecies;

    Entry flower;

    Entry insect;

    User user;

    @BeforeEach
    void init() throws IllegalAccessException {
        FieldUtils.writeField(identificationService, "validationCount", 2L, true);

        user = new User("test");
        flowerSpecies = new Species();
        flowerSpecies.setType(SpeciesType.Flower);
        insectSpecies = new Species();
        insectSpecies.setType(SpeciesType.Insect);
        flower = new Flower();
        insect = new Insect();
    }

    @Test
    @DisplayName("raise DataNotFoundException when no entry is found with given id during identification")
    void cannotCreateIdentificationWithoutEntry() {
        when(userProvider.getUserById(1L)).thenReturn(Optional.of(user));
        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> identificationService.identify(-1L, 1L, 1L));
        assertThat(exception.getMessage()).startsWith("Entry");
    }

    @Test
    @DisplayName("raise DataNotFoundException when no species is found with given id during identification")
    void cannotCreateIdentificationWithoutSpecies() {
        when(userProvider.getUserById(1L)).thenReturn(Optional.of(user));
        when(entryProvider.getEntryById(1L)).thenReturn(Optional.of(flower));
        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> identificationService.identify(1L, -1L, 1L));
        assertThat(exception.getMessage()).startsWith("Species");
    }

    @Test
    @DisplayName("raise DataNotFoundException when no user is found with given id during identification")
    void cannotCreateIdentificationWithoutUser() {
        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> identificationService.identify(1L, 1L, -1L));
        assertThat(exception.getMessage()).startsWith("User");
    }

    @Test
    @DisplayName("raise DuplicateDataException when an identification already exists for given entry and species")
    void cannotCreateIdentificationThatAlreadyExists() {
        when(userProvider.getUserById(1L)).thenReturn(Optional.of(user));
        when(speciesProvider.getSpeciesById(2L)).thenReturn(Optional.of(insectSpecies));
        when(entryProvider.getEntryById(2L)).thenReturn(Optional.of(insect));
        Identification identification = new Identification(insect, insectSpecies, user, new Date());
        when(identificationProvider.getIdentificationByEntryIdAndSpeciesId(2L, 2L)).thenReturn(Optional.of(identification));
        assertThrows(InvalidDataException.DuplicateDataException.class, () -> identificationService.identify(2L, 2L, 1L));
    }

    @Test
    @DisplayName("raise InvalidDataException when trying to identify a flower entry with an insect species")
    void cannotCreateIdentificationForInconsistentEntryAndSpecies() {
        when(userProvider.getUserById(1L)).thenReturn(Optional.of(user));
        when(speciesProvider.getSpeciesById(2L)).thenReturn(Optional.of(insectSpecies));
        when(entryProvider.getEntryById(1L)).thenReturn(Optional.of(flower));
        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> identificationService.identify(1L, 2L, 1L));
        assertThat(exception.getMessage()).contains("Flower");
    }

    @Test
    @DisplayName("raise InvalidDataException when trying to identify an insect entry with a flower species")
    void cannotCreateIdentificationForInconsistentEntryAndSpecies2() {
        when(userProvider.getUserById(1L)).thenReturn(Optional.of(user));
        when(speciesProvider.getSpeciesById(1L)).thenReturn(Optional.of(flowerSpecies));
        when(entryProvider.getEntryById(2L)).thenReturn(Optional.of(insect));
        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> identificationService.identify(2L, 1L, 1L));
        assertThat(exception.getMessage()).contains("Insect");
    }

    @Test
    @DisplayName("create an identification for given entry/species by given user")
    void createIdentificationForConsistentEntryAndSpeciesByKnownUser() throws DataNotFoundException, InvalidDataException {
        // Given
        when(userProvider.getUserById(1L)).thenReturn(Optional.of(user));
        when(speciesProvider.getSpeciesById(1L)).thenReturn(Optional.of(flowerSpecies));
        when(entryProvider.getEntryById(1L)).thenReturn(Optional.of(flower));
        when(identificationProvider.getIdentificationByEntryIdAndSpeciesId(anyLong(), anyLong())).thenReturn(Optional.empty());
        when(identificationProvider.save(Mockito.any())).then(AdditionalAnswers.returnsFirstArg());

        // When
        Identification identification = identificationService.identify(1L, 1L, 1L);

        // Then
        assertNotNull(identification, "Created identification should not be null for known entry, species and user ids.");
        assertThat(identification.getEntry()).isEqualTo(flower);
        assertThat(identification.getSpecies()).isEqualTo(flowerSpecies);
        assertThat(identification.getSuggestedBy()).isEqualTo(user);
    }

    @Test
    @DisplayName("raise DataNotFoundException when no user is found with given id to like identification")
    void cannotLikeIdentificationWithoutUser() {
        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> identificationService.like(1L, 1L, -1L));
        assertThat(exception.getMessage()).startsWith("User");
    }

    @Test
    @DisplayName("raise DataNotFoundException when no identification is found with given entry id to like identification")
    void cannotLikeIdentificationWithInvalidEntryId() {
        when(userProvider.getUserById(1L)).thenReturn(Optional.of(user));
        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> identificationService.like(-1L, 1L, 1L));
        assertThat(exception.getMessage()).startsWith("Identification");
    }

    @Test
    @DisplayName("raise DataNotFoundException when no identification is found with given species id to like identification")
    void cannotLikeIdentificationWithInvalidSpeciesId() {
        when(userProvider.getUserById(1L)).thenReturn(Optional.of(user));
        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> identificationService.like(1L, -1L, 1L));
        assertThat(exception.getMessage()).startsWith("Identification");
    }

    @Test
    @DisplayName("like identification for given entry/species by given user")
    void likeIdentification() throws DataNotFoundException {
        // Given
        User liker = new User("liker");
        when(userProvider.getUserById(2L)).thenReturn(Optional.of(liker));

        when(identificationProvider.save(Mockito.any())).then(AdditionalAnswers.returnsFirstArg());

        Identification identification = new Identification(flower, flowerSpecies, user, new Date());
        when(identificationProvider.getIdentificationByEntryIdAndSpeciesId(1L, 1L)).thenReturn(Optional.of(identification));

        // When
        Identification result = identificationService.like(1L, 1L, 2L);

        // Then
        assertThat(result.getLikes()).isNotNull().hasSize(1);
        assertThat(result.getLikes().iterator().next()).isEqualTo(liker);
    }

    @Test
    @DisplayName("dislike identification for given entry/species by given user if user has previoulsy like this identification")
    void dislikeIdentification() throws DataNotFoundException {
        // Given
        User liker = new User("liker");
        liker.setId(2L);
        when(userProvider.getUserById(2L)).thenReturn(Optional.of(liker));
        Identification identification = new Identification(flower, flowerSpecies, user, new Date());
        when(identificationProvider.getIdentificationByEntryIdAndSpeciesId(1L, 1L)).thenReturn(Optional.of(identification));
        identification.getLikes().add(liker);
        assertThat(identification.getLikes()).isNotNull().hasSize(1);
        when(identificationProvider.save(Mockito.any())).then(AdditionalAnswers.returnsFirstArg());

        // When
        Identification disliked = identificationService.like(1L, 1L, 2L);

        // Then
        assertThat(disliked.getLikes()).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("raise DataNotFoundException when no user is found with given id to validate identification")
    void cannotValidateIdentificationWithoutUser() {
        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> identificationService.validate(1L, 1L, -1L));
        assertThat(exception.getMessage()).startsWith("User");
    }

    @Test
    @DisplayName("raise DataNotFoundException when no identification is found with given entry and species ids to like identification")
    void cannotValidateNonExistingIdentification() {
        when(userProvider.getUserById(1L)).thenReturn(Optional.of(user));
        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> identificationService.validate(2L, 1L, 1L));
        assertThat(exception.getMessage()).startsWith("Identification");
    }

    @Test
    @DisplayName("raise ValidationPermissionException when validator has not enough right to validate a flower identification")
    void userCannotValidateFlowerIdentificationWithoutBeingFlowerValidator() {
        when(userProvider.getUserById(3L)).thenReturn(Optional.of(new User("validator")));
        Identification identification = new Identification(flower, flowerSpecies, user, new Date());
        when(identificationProvider.getIdentificationByEntryIdAndSpeciesId(1L, 1L)).thenReturn(Optional.of(identification));
        UserAccountException.ValidationPermissionException exception = assertThrows(UserAccountException.ValidationPermissionException.class, () -> identificationService.validate(1L, 1L, 3L));
        assertThat(exception.getMessage()).contains("Flower");
    }

    @Test
    @DisplayName("raise ValidationPermissionException when validator has not enough right to validate an insect identification")
    void userCannotValidateInsectIdentificationWithoutBeingInsectValidator() {
        when(userProvider.getUserById(3L)).thenReturn(Optional.of(new User("validator")));
        Identification identification = new Identification(insect, insectSpecies, user, new Date());
        when(identificationProvider.getIdentificationByEntryIdAndSpeciesId(2L, 2L)).thenReturn(Optional.of(identification));
        UserAccountException.ValidationPermissionException exception = assertThrows(UserAccountException.ValidationPermissionException.class, () -> identificationService.validate(2L, 2L, 3L));
        assertThat(exception.getMessage()).contains("Insect");
    }

    @Test
    @DisplayName("raise AlreadyValidatedDataException when entry identification has already been validated")
    void cannotValidateIdentificationForAlreadyIdentifiedEntry() {
        // Given
        User flowerValidator = new User("flower_validator");
        flowerValidator.setFlowerValidator(true);
        when(userProvider.getUserById(2L)).thenReturn(Optional.of(flowerValidator));
        Entry entry = new Flower();
        entry.setValidated(true);
        Identification identification = new Identification(entry, flowerSpecies, user, new Date());
        when(identificationProvider.getIdentificationByEntryIdAndSpeciesId(1L, 1L)).thenReturn(Optional.of(identification));

        // When
        assertThrows(InvalidDataException.AlreadyValidatedDataException.class, () -> identificationService.validate(1L, 1L, 2L));
    }

    @Test
    @DisplayName("raise ValidationPermissionException when a validator tries to validate its own identification")
    void userCannotValidateItsOwnSuggestedIdentification() {
        // Given
        User user = new User("user");
        user.setFlowerValidator(true);
        when(userProvider.getUserById(1L)).thenReturn(Optional.of(user));
        Identification identification = new Identification(flower, flowerSpecies, user, new Date());
        when(identificationProvider.getIdentificationByEntryIdAndSpeciesId(1L, 1L)).thenReturn(Optional.of(identification));

        // When
        assertThrows(UserAccountException.ValidationPermissionException.class, () -> identificationService.validate(1L, 1L, 1L));
    }

    @Test
    @DisplayName("validate identification for given flower/species by given flower validator")
    void validateFlowerIdentificationByFlowerValidator() throws DataNotFoundException, InvalidDataException, UserAccountException.ValidationPermissionException {
        // Given
        User flowerValidator = new User("flower_validator");
        flowerValidator.setFlowerValidator(true);
        when(userProvider.getUserById(2L)).thenReturn(Optional.of(flowerValidator));
        Identification identification = new Identification(flower, flowerSpecies, user, new Date());
        when(identificationProvider.getIdentificationByEntryIdAndSpeciesId(1L, 1L)).thenReturn(Optional.of(identification));
        when(identificationProvider.save(Mockito.any())).then(AdditionalAnswers.returnsFirstArg());

        // When
        Identification result = identificationService.validate(1L, 1L, 2L);

        // Then
        assertNotNull(result, "Validated identification should not be null for valid validator and entry & species ids.");
        assertThat(result.getValidatedBy()).isEqualTo(flowerValidator);
    }

    @Test
    @DisplayName("validate identification for given insect/species by given insect validator")
    void validateInsectIdentificationByInsectValidator() throws DataNotFoundException, InvalidDataException, UserAccountException.ValidationPermissionException {
        // Given
        User insectValidator = new User("insect_validator");
        insectValidator.setInsectValidator(true);
        when(userProvider.getUserById(3L)).thenReturn(Optional.of(insectValidator));
        Identification identification = new Identification(insect, insectSpecies, user, new Date());
        when(identificationProvider.getIdentificationByEntryIdAndSpeciesId(2L, 2L)).thenReturn(Optional.of(identification));
        when(identificationProvider.save(Mockito.any())).then(AdditionalAnswers.returnsFirstArg());

        // When
        Identification result = identificationService.validate(2L, 2L, 3L);

        // Then
        assertNotNull(result, "Validated identification should not be null for valid validator and entry & species ids.");
        assertThat(result.getValidatedBy()).isEqualTo(insectValidator);

    }

    @Test
    @DisplayName("raise DataNotFoundException when giving validator rights to null user")
    void cannotGiveValidatorRightsToNullUser() {
        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> identificationService.giveValidatorRights(null));
        assertThat(exception.getMessage()).isEqualTo("Null user");
    }

    @Test
    @DisplayName("do nothing if user is already validator")
    void cannotGiveValidatorRightsToUserWhichIsAlreadyValidator() throws DataNotFoundException {
        // Given
        User validator = new User("validator");
        validator.setFlowerValidator(true);
        validator.setInsectValidator(true);
        when(identificationProvider.getIdentificationsBySuggestedByUser(validator))
                .thenReturn(Collections.singletonList(new Identification(flower, flowerSpecies, user, new Date())));

        // When
        identificationService.giveValidatorRights(validator);

        // Then
        verify(userProvider, never()).save(any());
    }

    @Test
    @DisplayName("give flower validator right to user who has enough flower identification validatior")
    void giveFlowerValidatorRightToUserWhoHasValidatedEnoughFlowerIdentification() throws DataNotFoundException {
        // Given
        User validator = new User("validator");
        when(identificationProvider.getIdentificationsBySuggestedByUser(validator))
                .thenReturn(List.of(createValidatedIdentification(flower, flowerSpecies, user, validator),
                        createValidatedIdentification(flower, flowerSpecies, user, validator),
                        createValidatedIdentification(flower, flowerSpecies, user, validator),
                        createValidatedIdentification(flower, flowerSpecies, user, validator)));

        // When
        identificationService.giveValidatorRights(validator);

        // Then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userProvider).save(userCaptor.capture());
        User saved = userCaptor.getValue();
        assertTrue(saved.isFlowerValidator());
    }

    @Test
    @DisplayName("give insect validator right to user who has enough insect identification validatior")
    void giveInsectValidatorRightToUserWhoHasValidatedEnoughInsectIdentification() throws DataNotFoundException {
        // Given
        User validator = new User("validator");
        when(identificationProvider.getIdentificationsBySuggestedByUser(validator))
                .thenReturn(List.of(createValidatedIdentification(insect, insectSpecies, user, validator),
                        createValidatedIdentification(insect, insectSpecies, user, validator),
                        createValidatedIdentification(insect, insectSpecies, user, validator)));

        // When
        identificationService.giveValidatorRights(validator);

        // Then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userProvider).save(userCaptor.capture());
        User saved = userCaptor.getValue();
        assertTrue(saved.isInsectValidator());
    }

    @Test
    @DisplayName("not give insect validator right to user who has not enough insect identification validatior")
    void dontGiveInsectValidatorRightToUserWhoHasNotValidatedEnoughInsectIdentification() throws DataNotFoundException {
        // Given
        User validator = new User("validator");
        when(identificationProvider.getIdentificationsBySuggestedByUser(validator))
                .thenReturn(List.of(createValidatedIdentification(insect, insectSpecies, user, validator)));

        // When
        identificationService.giveValidatorRights(validator);

        // Then
        verify(userProvider, never()).save(any());
    }

    @Test
    @DisplayName("not give flower validator right to user who has not enough flower identification validatior")
    void dontGiveFlowerValidatorRightToUserWhoHasNotValidatedEnoughFlowerIdentification() throws DataNotFoundException {
        // Given
        User validator = new User("validator");
        when(identificationProvider.getIdentificationsBySuggestedByUser(validator))
                .thenReturn(List.of(createValidatedIdentification(flower, flowerSpecies, user, validator)));

        // When
        identificationService.giveValidatorRights(validator);

        // Then
        verify(userProvider, never()).save(any());
    }

    private Identification createValidatedIdentification(Entry flower, Species flowerSpecies, User user, User validator) {
        Identification identification = new Identification(flower, flowerSpecies, user, new Date());
        identification.setValidatedBy(validator);
        identification.setValidatedDate(new Date());
        return identification;
    }

    @Test
    @DisplayName("return identifications for given entry id.")
    void getIdentificationsForEntry() {
        // Given
        List<Identification> identifications = Collections.singletonList(new Identification(flower, flowerSpecies, user, new Date()));
        when(identificationProvider.getIdentificationsByEntryId(1L)).thenReturn(identifications);

        // When
        List<Identification> found = identificationService.getIdentificationsForEntry(1L);

        // Then
        assertThat(found).isEqualTo(identifications);
    }

    @Test
    @DisplayName("raise DataNotFoundException when no user is found with given id to comment identification")
    void cannotCommentIdentificationWithoutUser() {
        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> identificationService.comment(1L, 1L, -1L, "comment"));
        assertThat(exception.getMessage()).startsWith("User");
    }

    @Test
    @DisplayName("raise DataNotFoundException when no identification is found with given entry id to comment identification")
    void cannotCommentIdentificationWithInvalidEntryId() {
        when(userProvider.getUserById(1L)).thenReturn(Optional.of(user));
        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> identificationService.comment(-1L, 1L, 1L, "comment"));
        assertThat(exception.getMessage()).startsWith("Identification");
    }

    @Test
    @DisplayName("raise DataNotFoundException when no identification is found with given species id to like identification")
    void cannotCommentIdentificationWithInvalidSpeciesId() {
        when(userProvider.getUserById(1L)).thenReturn(Optional.of(user));
        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> identificationService.comment(1L, -1L, 1L, "comment"));
        assertThat(exception.getMessage()).startsWith("Identification");
    }

    @Test
    @DisplayName("comment identification for given entry/species by given user")
    void commentIdentification() throws DataNotFoundException {
        // Given
        String comment = "comment";
        User commenter = new User("commenter");
        when(userProvider.getUserById(2L)).thenReturn(Optional.of(commenter));
        Identification identification = new Identification(flower, flowerSpecies, user, new Date());
        when(identificationProvider.getIdentificationByEntryIdAndSpeciesId(1L, 1L)).thenReturn(Optional.of(identification));
        when(commentProvider.save(Mockito.any())).then(AdditionalAnswers.returnsFirstArg());

        // When
        Comment result = identificationService.comment(1L, 1L, 2L, comment);

        // Then
        assertThat(result.getCommentedBy()).isEqualTo(commenter);
        assertThat(result.getText()).isEqualTo(comment);
    }

    @Test
    @DisplayName("raise DataNotFoundException when no identification is found with given species id to get identification comments")
    void cannotGetIdentificationCommentsWithInvalidSpeciesId() {
        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> identificationService.getIdentificationComments(1L, -1L));
        assertThat(exception.getMessage()).startsWith("Identification");
    }

    @Test
    @DisplayName("get comments of identification with given entry/species")
    void getIdentificationComments() throws DataNotFoundException {
        // Given
        Identification identification = new Identification(flower, flowerSpecies, user, new Date());
        when(identificationProvider.getIdentificationByEntryIdAndSpeciesId(1L, 1L)).thenReturn(Optional.of(identification));
        when(commentProvider.getCommentsByIdentification(identification)).thenReturn(Collections.singletonList(new Comment("comment", new User(), new Date())));

        // When
        List<Comment> result = identificationService.getIdentificationComments(1L, 1L);

        // Then
        assertThat(result).isNotNull().hasSize(1);
    }

}
