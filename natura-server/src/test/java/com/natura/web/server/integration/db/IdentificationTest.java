package com.natura.web.server.integration.db;

import com.natura.web.server.entities.Comment;
import com.natura.web.server.entities.Entry;
import com.natura.web.server.entities.Flower;
import com.natura.web.server.entities.Identification;
import com.natura.web.server.entities.Insect;
import com.natura.web.server.entities.Species;
import com.natura.web.server.entities.User;
import com.natura.web.server.exceptions.DataNotFoundException;
import com.natura.web.server.repository.CommentRepository;
import com.natura.web.server.repository.EntryRepository;
import com.natura.web.server.repository.IdentificationRepository;
import com.natura.web.server.repository.SpeciesRepository;
import com.natura.web.server.repository.UserRepository;
import com.natura.web.server.services.EntryService;
import com.natura.web.server.services.IdentificationService;
import com.natura.web.server.services.ImageService;
import com.natura.web.server.services.UserService;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
@ActiveProfiles("test")
class IdentificationTest {

  @Autowired
  EntryService entryService;

  @Autowired
  ImageService imageService;

  @Autowired
  UserService userService;

  @Autowired
  IdentificationService identificationService;

  @Autowired
  EntryRepository entryRepository;

  @Autowired
  SpeciesRepository speciesRepository;

  @Autowired
  IdentificationRepository identificationRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  CommentRepository commentRepository;

  Long userId = -1L;

  Long speciesId = -1L;

  Long entryId = -1L;

  Long validatorId = -1L;

  @BeforeEach
  void init() {

    String username = "testUser";
    User user = userRepository.findByUsername(username);
    if (user == null) {
      // Create an user
      user = new User();
      user.setUsername(username);
      user.setEmail("test@email.com");
      user.setPassword("pwd");
      user = userRepository.save(user);
    }
    this.userId = user.getId();

    String validatorUsername = "validator";
    User validator = userRepository.findByUsername(validatorUsername);
    if (validator == null) {
      // Create an user
      validator = new User();
      validator.setUsername(validatorUsername);
      validator.setEmail("validator@email.com");
      validator.setPassword("pwd");
      validator = userRepository.save(validator);
    }
    this.validatorId = validator.getId();

    String speciesName = "commonName";
    Species species = speciesRepository.findByCommonName(speciesName);
    if (species == null) {
      // Create a species
      species = new Species();
      species.setCommonName(speciesName);
      species.setScientificName("scientificName");
      species.setType(Species.Type.FLOWER);
      species = speciesRepository.save(species);
    }
    this.speciesId = species.getId();

    // Create an entry
    Flower entry = new Flower("testFlower", new Date(), "description", "location");
    entry = entryRepository.save(entry);
    this.entryId = entry.getId();
  }

  @Test
  void createIdentification() {
    Entry entry = entryRepository.findById(this.entryId).orElse(null);
    Species species = speciesRepository.findById(this.speciesId).orElse(null);
    User user = userRepository.findById(this.userId).orElse(null);

    Identification identification = new Identification(entry, species, user, new Date());
    identification = identificationRepository.save(identification);

    Assertions.assertNotNull(identification, "Saved identification cannot be null.");
    Assertions.assertNotNull(identification.getId(), "Saved identification id cannot be null.");
  }

  @Test
  void createEntryWithSuggestedIdentification() throws IOException, DataNotFoundException {
    Entry newFlower = new Flower("roseFlower", new Date(), "desc", "France");

    File file = new File(getClass().getClassLoader().getResource("images/pexels-photo-133472.jpeg").getFile());
    InputStream is = new FileInputStream(file);
    MultipartFile picture = new MockMultipartFile("flower_picture", is);

    Entry saved = entryService.create(newFlower, picture, this.userId, this.speciesId);

    Assertions.assertNotNull(saved, "Saved entry cannot be null.");

    List<Identification> identifications = identificationRepository.findByIdEntryId(saved.getId());
    Assertions.assertTrue(identifications != null && identifications.size() == 1);
  }

  @Test
  void getIdentificationFromEntryAndSpeciesId() {
    // First create and save data
    // create a species
    Species species = new Species();
    species.setCommonName("Azuré bleu céleste");
    species.setScientificName("Lysandra bellargus");
    species.setType(Species.Type.INSECT);
    species = speciesRepository.save(species);
    // create an entry
    Insect entry = new Insect("testButterfly", new Date(), "description", "location");
    entry = entryRepository.save(entry);
    // create the identification
    User user = userRepository.findById(this.userId).orElse(null);
    Identification identification = new Identification(entry, species, user, new Date());
    identificationRepository.save(identification);

    // Retrieve identification by entry id and species id
    Identification found = identificationRepository.findByIdEntryIdAndIdSpeciesId(entry.getId(), species.getId());
    Assertions.assertNotNull(found);
  }

  private Identification createIdentification(Species.Type type, String entryName, User user, String speciesName, boolean isValidated) {
    Entry entry = null;
    if (type == Species.Type.FLOWER) {
      entry = new Flower();
    } else if (type == Species.Type.INSECT) {
      entry = new Insect();
    }

    Assertions.assertTrue(entry != null);
    entry.setName(entryName);
    entry.setDate(new Date());
    entry.setValidated(isValidated);
    entry = entryRepository.save(entry);

    Species species = new Species();
    species.setType(type);
    species.setCommonName(speciesName);
    species.setScientificName(speciesName);
    species = speciesRepository.save(species);

    Identification identification = new Identification(entry, species, user, new Date());
    if (isValidated) {
      User validator = userRepository.findById(this.validatorId).orElse(null);
      identification.setValidatedBy(validator);
      identification.setValidatedDate(new Date());
    }
    return identificationRepository.save(identification);
  }

  @Test
  void getIdentificationsSuggestedByOneUser() {
    // Create an user
    User user = new User("testUser2");
    user.setEmail("test2@email.com");
    user.setPassword("pwd");
    user = userRepository.save(user);

    createIdentification(Species.Type.FLOWER, "item1", user, "species_flower1", true);
    createIdentification(Species.Type.INSECT, "item2", user, "species_insect1", true);
    createIdentification(Species.Type.FLOWER, "item3", user, "species_flower2", false);

    List<Identification> identifications = identificationRepository.findBySuggestedBy(user);
    Assertions.assertTrue(identifications != null && identifications.size() == 3);
  }

  @Test
  void cannotGiveValidatorRightsForTwoValidatedIdentifications() throws DataNotFoundException {
    // Create an user
    String username = "testUser3";
    User user = new User(username);
    user.setEmail("test3@email.com");
    user.setPassword("pwd");
    user = userRepository.save(user);

    createIdentification(Species.Type.FLOWER, "element1", user, "flower1", true);
    createIdentification(Species.Type.INSECT, "element2", user, "insect1", true);
    createIdentification(Species.Type.FLOWER, "element3", user, "flower2", true);
    createIdentification(Species.Type.FLOWER, "element4", user, "flower3", false);

    identificationService.giveValidatorRights(user);
    user = userRepository.findByUsername(username);
    Assertions.assertTrue(!user.isFlowerValidator());
  }

  @Test
  void giveValidatorRightsForThreeFlowerValidatedIdentifications() throws DataNotFoundException {
    // Create an user
    String username = "testUser4";
    User user = new User(username);
    user.setEmail("test4@email.com");
    user.setPassword("pwd");
    user = userRepository.save(user);

    createIdentification(Species.Type.FLOWER, "el1", user, "plant1", true);
    createIdentification(Species.Type.FLOWER, "el2", user, "plant2", true);
    createIdentification(Species.Type.FLOWER, "el3", user, "plant3", true);

    identificationService.giveValidatorRights(user);
    user = userRepository.findByUsername(username);
    Assertions.assertTrue(user.isFlowerValidator());
  }

  @Test
  void createAndCommentIdentification() throws DataNotFoundException {
    // Create an user
    User user = new User("observer");
    user.setEmail("observer@email.com");
    user.setPassword("pwd");
    user = userRepository.save(user);

    Identification created = createIdentification(Species.Type.FLOWER, "red_flower", user, "Rosaceae", false);
    Long speciesId = created.getSpecies().getId();
    Long entryId = created.getEntry().getId();

    Comment comment = identificationService.comment(entryId, speciesId, user.getId(), "this is a comment to an identification");

    Identification identification = identificationRepository.findByIdEntryIdAndIdSpeciesId(entryId, speciesId);
    List<Comment> comments = commentRepository.findByIdentification(identification);
    Assertions.assertTrue(comments != null && comments.size() == 1);
  }

  @Test
  void likeIdentification() throws DataNotFoundException {
    // Create an user
    User user = new User("liker");
    user.setEmail("liker@email.com");
    user.setPassword("pwd");
    user = userRepository.save(user);

    Identification created = createIdentification(Species.Type.FLOWER, "blue_flower", user, "Cyanus segetum", false);
    Long speciesId = created.getSpecies().getId();
    Long entryId = created.getEntry().getId();

    Identification liked = identificationService.like(entryId, speciesId, user.getId());

    Identification identification = identificationRepository.findByIdEntryIdAndIdSpeciesId(entryId, speciesId);
    Set<User> likes = identification.getLikes();
    Assertions.assertTrue(likes != null && likes.size() == 1 && likes.iterator().next().getId().equals(user.getId()));

  }

  @Test
  void dislikeIdentification() throws DataNotFoundException {
    // Create an user
    User user = new User("liker2");
    user.setEmail("liker2@email.com");
    user.setPassword("pwd");
    user = userRepository.save(user);

    Identification created = createIdentification(Species.Type.FLOWER, "white_flower", user, "Orchidaceae", false);
    Long speciesId = created.getSpecies().getId();
    Long entryId = created.getEntry().getId();

    created.getLikes().add(user);
    identificationRepository.save(created);
    Assertions.assertTrue(
        created.getLikes() != null && created.getLikes().size() == 1 && created.getLikes().iterator().next().getId().equals(user.getId()));

    Identification disliked = identificationService.like(entryId, speciesId, user.getId());

    Identification identification = identificationRepository.findByIdEntryIdAndIdSpeciesId(entryId, speciesId);
    Set<User> likes = identification.getLikes();
    Assertions.assertTrue(likes != null && likes.size() == 0);

  }

}
