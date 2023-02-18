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

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class IdentificationService {

    private final UserProvider userProvider;

    private final EntryProvider entryProvider;

    private final SpeciesProvider speciesProvider;

    private final IdentificationProvider identificationProvider;

    private final CommentProvider commentProvider;

    private final Long validationCount;

    public IdentificationService(final UserProvider userProvider,
                        final EntryProvider entryProvider,
                        final SpeciesProvider speciesProvider,
                        final IdentificationProvider identificationProvider,
                        final CommentProvider commentProvider,
                        final Long validationCount) {
        this.userProvider = userProvider;
        this.entryProvider = entryProvider;
        this.speciesProvider = speciesProvider;
        this.identificationProvider = identificationProvider;
        this.commentProvider = commentProvider;
        this.validationCount = validationCount;
    }

    public Identification identify(Long entryId, Long speciesId, Long userId)
            throws DataNotFoundException, InvalidDataException {

        // Get proposer user
        Optional<User> suggestedBy = userProvider.getUserById(userId);
        if (suggestedBy.isEmpty()) {
            throw new DataNotFoundException(User.class, "id", userId);
        }

        // Get entry
        Optional<Entry> entry = entryProvider.getEntryById(entryId);
        if (entry.isEmpty()) {
            throw new DataNotFoundException(Entry.class, "id", entryId);
        }

        // Get species
        Optional<Species> species = speciesProvider.getSpeciesById(speciesId);
        if (species.isEmpty()) {
            throw new DataNotFoundException(Species.class, "id", speciesId);
        }

        // Check no identification already exists for entry + species couple
        Optional<Identification> duplicate = identificationProvider.getIdentificationByEntryIdAndSpeciesId(entryId, speciesId);
        if (duplicate.isPresent()) {
            throw new InvalidDataException.DuplicateDataException("Identification already exists for entry "
                    + entry.get().getName() + " and species " + species.get().getCommonName() + ".");
        }

        // Check species and entry types are consistent
        if (entry.get() instanceof Flower && species.get().getType() != SpeciesType.Flower) {
            throw new InvalidDataException("Suggested species must be of type Flower for a Flower entry.");
        } else if (entry.get() instanceof Insect && species.get().getType() != SpeciesType.Insect) {
            throw new InvalidDataException("Suggested species must be of type Insect for an Insect entry.");
        }

        Identification identification = new Identification(entry.get(), species.get(), suggestedBy.get(), new Date());
        return identificationProvider.save(identification);

    }

    public Identification validate(Long entryId, Long speciesId, Long userId)
            throws DataNotFoundException, InvalidDataException, UserAccountException.ValidationPermissionException {

        // Get validator user
        Optional<User> validator = userProvider.getUserById(userId);
        if (validator.isEmpty()) {
            throw new DataNotFoundException(User.class, "id", userId);
        }

        // Get identification
        Optional<Identification> opt = identificationProvider.getIdentificationByEntryIdAndSpeciesId(entryId, speciesId);
        if (opt.isEmpty()) {
            throw new DataNotFoundException(Identification.class, "id", "{ entry: " + entryId + ", species: " + speciesId + " }");
        }
        Identification identification = opt.get();
        // Check entry is not null
        if (identification.getEntry() == null) {
            throw new InvalidDataException("Invalid entry of identification { entry: " + entryId + ", species: " + speciesId + " }");
        }
        // Check entry has not been validated yet
        else if (identification.getEntry().isValidated()) {
            throw new InvalidDataException.AlreadyValidatedDataException("Entry " + identification.getEntry().getName());
        }
        // Check user has the right to validate this identification
        else if (identification.getEntry() instanceof Flower) {
            if (!validator.get().isFlowerValidator())
                throw new UserAccountException.ValidationPermissionException(validator.get().getUsername(), SpeciesType.Flower);
        } else if (identification.getEntry() instanceof Insect) {
            if (!validator.get().isInsectValidator())
                throw new UserAccountException.ValidationPermissionException(validator.get().getUsername(), SpeciesType.Insect);
        } else {
            throw new InvalidDataException("Invalid entry of identification { entry : " + entryId + ", species: " + speciesId + " }");
        }

        if (identification.getSuggestedBy() != null && validator.get().getUsername().equals(identification.getSuggestedBy().getUsername())) {
            throw new UserAccountException.ValidationPermissionException("User has not the permission to validate its own suggested identification.");
        }

        // validate identification and save
        identification.setValidatedBy(validator.get());
        identification.setValidatedDate(new Date());
        Identification validIdentification = identificationProvider.save(identification);

        // set entry as validated and save
        Entry entry = identification.getEntry();
        entry.setValidated(true);
        entryProvider.save(entry);

        return validIdentification;
    }

    public void giveValidatorRights(User user) throws DataNotFoundException {
        boolean becomeValidator = false;

        if (user == null)
            throw new DataNotFoundException("Null user");

        List<Identification> identifications = identificationProvider.getIdentificationsBySuggestedByUser(user);

        if (!identifications.isEmpty()) {
            // Evaluate flower validator rights
            if (!user.isFlowerValidator()) {
                Long validated = identifications.stream()
                        .filter(i -> i.getValidatedBy() != null
                                && i.getEntry() != null
                                && i.getEntry() instanceof Flower)
                        .count();
                if (validated >= validationCount) {
                    user.setFlowerValidator(true);
                    becomeValidator = true;
                }
            }

            // Evaluate insect validator rights
            if (!user.isInsectValidator()) {
                Long validated = identifications.stream()
                        .filter(i -> i.getValidatedBy() != null
                                && i.getEntry() != null
                                && i.getEntry() instanceof Insect)
                        .count();
                if (validated >= validationCount) {
                    user.setInsectValidator(true);
                    becomeValidator = true;
                }
            }

            // update user if he gets validator rights
            if (becomeValidator) {
                userProvider.save(user);
            }
        }
    }

    public Comment comment(Long entryId, Long speciesId, Long userId, String text)
            throws DataNotFoundException {
        // Get commentator user
        User observer = userProvider.getUserById(userId).orElse(null);
        if (observer == null) {
            throw new DataNotFoundException(User.class, "id", userId);
        }

        // Get identification
        Optional<Identification> identification = identificationProvider.getIdentificationByEntryIdAndSpeciesId(entryId, speciesId);
        if (identification.isEmpty()) {
            throw new DataNotFoundException(Identification.class, "id", "{ entry: " + entryId + ", species: " + speciesId + " }");
        }

        // Create comment and linked it to identification
        Comment comment = new Comment(text, observer, new Date());
        comment.setIdentification(identification.get());
        return commentProvider.save(comment);
    }

    public List<Comment> getIdentificationComments(Long entryId, Long speciesId) throws DataNotFoundException {
        // Get identification
        Optional<Identification> identification = identificationProvider.getIdentificationByEntryIdAndSpeciesId(entryId, speciesId);
        if (identification.isEmpty()) {
            throw new DataNotFoundException(Identification.class, "id", "{ entry: " + entryId + ", species: " + speciesId + " }");
        }

        return commentProvider.getCommentsByIdentification(identification.get());
    }

    public Identification like(Long entryId, Long speciesId, Long userId) throws DataNotFoundException {
        // Get user
        User user = userProvider.getUserById(userId).orElse(null);
        if (user == null) {
            throw new DataNotFoundException(User.class, "id", userId);
        }

        // Get identification
        Optional<Identification> identification = identificationProvider.getIdentificationByEntryIdAndSpeciesId(entryId, speciesId);
        if (identification.isEmpty()) {
            throw new DataNotFoundException(Identification.class, "id", "{ entry: " + entryId + ", species: " + speciesId + " }");
        }

        if (identification.get().getLikes() == null) {
            Set<User> likes = new HashSet<>();
            likes.add(user);
            identification.get().setLikes(likes);
        } else {
            boolean alreadyLikedByUser = identification.get().getLikes().stream().anyMatch(p -> p != null && p.getId().equals(userId));
            // If user already liked the identification
            if (alreadyLikedByUser) {
                // remove his like
                identification.get().getLikes().removeIf(p -> p != null && p.getId().equals(userId));
            } else {
                // add his like
                identification.get().getLikes().add(user);
            }
        }

        return identificationProvider.save(identification.get());
    }

    public List<Identification> getIdentificationsForEntry(Long entryId) {
        return identificationProvider.getIdentificationsByEntryId(entryId);
    }
}
