package com.natura.web.server.services;

import com.natura.web.server.entities.Comment;
import com.natura.web.server.entities.Entry;
import com.natura.web.server.entities.Flower;
import com.natura.web.server.entities.Identification;
import com.natura.web.server.entities.Insect;
import com.natura.web.server.entities.Species;
import com.natura.web.server.entities.User;
import com.natura.web.server.exceptions.DataNotFoundException;
import com.natura.web.server.exceptions.InvalidDataException;
import com.natura.web.server.exceptions.UserAccountException;
import com.natura.web.server.repository.CommentRepository;
import com.natura.web.server.repository.EntryRepository;
import com.natura.web.server.repository.IdentificationRepository;
import com.natura.web.server.repository.SpeciesRepository;
import com.natura.web.server.repository.UserRepository;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class IdentificationService {

  private final UserRepository userRepository;

  private final EntryRepository entryRepository;

  private final SpeciesRepository speciesRepository;

  private final IdentificationRepository identificationRepository;

  private final CommentRepository commentRepository;

  @Value("${validation.countNeeded}")
  private Long validationCount;

  public IdentificationService(UserRepository userRepository, EntryRepository entryRepository,
      SpeciesRepository speciesRepository, IdentificationRepository identificationRepository,
      CommentRepository commentRepository) {
    this.userRepository = userRepository;
    this.entryRepository = entryRepository;
    this.speciesRepository = speciesRepository;
    this.identificationRepository = identificationRepository;
    this.commentRepository = commentRepository;
  }

  public Identification identify(Long entryId, Long speciesId, Long userId)
      throws DataNotFoundException, InvalidDataException {

    // Get proposer user
    User suggestedBy = userRepository.findById(userId).orElse(null);
    if (suggestedBy == null) {
      throw new DataNotFoundException(User.class, "id", userId);
    }

    // Get entry
    Entry entry = entryRepository.findById(entryId).orElse(null);
    if (entry == null) {
      throw new DataNotFoundException(Entry.class, "id", entryId);
    }

    // Get species
    Species species = speciesRepository.findById(speciesId).orElse(null);
    if (species == null) {
      throw new DataNotFoundException(Species.class, "id", speciesId);
    }

    // Check no identification already exists for entry + species couple
    Identification duplicate = identificationRepository.findByIdEntryIdAndIdSpeciesId(entryId, speciesId);
    if (duplicate != null) {
      throw new InvalidDataException.DuplicateDataException("Identification already exists for entry "
          + entry.getName() + " and species " + species.getCommonName() + ".");
    }

    // Check species and entry types are consistent
    if (entry instanceof Flower && species.getType() != Species.Type.FLOWER) {
      throw new InvalidDataException("Suggested species must be of type Flower for a Flower entry.");
    } else if (entry instanceof Insect && species.getType() != Species.Type.INSECT) {
      throw new InvalidDataException("Suggested species must be of type Insect for an Insect entry.");
    }

    Identification identification = new Identification(entry, species, suggestedBy, new Date());
    return identificationRepository.save(identification);

  }

  public Identification validate(Long entryId, Long speciesId, Long userId)
      throws DataNotFoundException, InvalidDataException, UserAccountException.ValidationPermissionException {

    // Get validator user
    User validator = userRepository.findById(userId).orElse(null);
    if (validator == null) {
      throw new DataNotFoundException(User.class, "id", userId);
    }

    // Get identification
    Identification identification = identificationRepository.findByIdEntryIdAndIdSpeciesId(entryId, speciesId);
    if (identification == null) {
      throw new DataNotFoundException(Identification.class, "id", "{ entry: " + entryId + ", species: " + speciesId + " }");
    }

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
        if (!validator.isFlowerValidator()) {
            throw new UserAccountException.ValidationPermissionException(validator.getUsername(), Species.Type.FLOWER);
        }
    } else if (identification.getEntry() instanceof Insect) {
        if (!validator.isInsectValidator()) {
            throw new UserAccountException.ValidationPermissionException(validator.getUsername(), Species.Type.INSECT);
        }
    } else {
      throw new InvalidDataException("Invalid entry of identification { entry : " + entryId + ", species: " + speciesId + " }");
    }

    if (identification.getSuggestedBy() != null && validator.getUsername().equals(identification.getSuggestedBy().getUsername())) {
      throw new UserAccountException.ValidationPermissionException(
          "User has not the permission to validate its own suggested identification.");
    }

    // validate identification and save
    identification.setValidatedBy(validator);
    identification.setValidatedDate(new Date());
    Identification validIdentification = identificationRepository.save(identification);

    // set entry as validated and save
    Entry entry = identification.getEntry();
    entry.setValidated(true);
    entryRepository.save(entry);

    return validIdentification;
  }

  public void giveValidatorRights(User user) throws DataNotFoundException {
    boolean becomeValidator = false;

      if (user == null) {
          throw new DataNotFoundException("Null user");
      }

    List<Identification> identifications = identificationRepository.findBySuggestedBy(user);

    if (identifications.isEmpty()) {
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
        userRepository.save(user);
      }
    }
  }

  public Comment comment(Long entryId, Long speciesId, Long userId, String text)
      throws DataNotFoundException {
    // Get commentator user
    User observer = userRepository.findById(userId).orElse(null);
    if (observer == null) {
      throw new DataNotFoundException(User.class, "id", userId);
    }

    // Get identification
    Identification identification = identificationRepository.findByIdEntryIdAndIdSpeciesId(entryId, speciesId);
    if (identification == null) {
      throw new DataNotFoundException(Identification.class, "id", "{ entry: " + entryId + ", species: " + speciesId + " }");
    }

    // Create comment and linked it to identification
    Comment comment = new Comment(text, observer, new Date());
    comment.setIdentification(identification);
    return commentRepository.save(comment);
  }

  public List<Comment> getIdentificationComments(Long entryId, Long speciesId) throws DataNotFoundException {
    // Get identification
    Identification identification = identificationRepository.findByIdEntryIdAndIdSpeciesId(entryId, speciesId);
    if (identification == null) {
      throw new DataNotFoundException(Identification.class, "id", "{ entry: " + entryId + ", species: " + speciesId + " }");
    }
    return commentRepository.findByIdentification(identification);
  }

  public Identification like(Long entryId, Long speciesId, Long userId) throws DataNotFoundException {
    // Get user
    User user = userRepository.findById(userId).orElse(null);
    if (user == null) {
      throw new DataNotFoundException(User.class, "id", userId);
    }

    // Get identification
    Identification identification = identificationRepository.findByIdEntryIdAndIdSpeciesId(entryId, speciesId);
    if (identification == null) {
      throw new DataNotFoundException(Identification.class, "id", "{ entry: " + entryId + ", species: " + speciesId + " }");
    }

    if (identification.getLikes() == null) {
      Set<User> likes = new HashSet<>();
      likes.add(user);
      identification.setLikes(likes);
    } else {
      boolean alreadyLikedByUser = identification.getLikes().stream().anyMatch(p -> p != null && p.getId().equals(userId));
      // If user already liked the identification
      if (alreadyLikedByUser) {
        // remove his like
        identification.getLikes().removeIf(p -> p != null && p.getId().equals(userId));
      } else {
        // add his like
        identification.getLikes().add(user);
      }
    }

    return identificationRepository.save(identification);
  }
}
