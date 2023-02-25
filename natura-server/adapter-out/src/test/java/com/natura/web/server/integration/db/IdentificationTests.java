package com.natura.web.server.integration.db;

import com.natura.web.server.exception.DataNotFoundException;
import com.natura.web.server.exception.InvalidDataException.InvalidFileException;
import com.natura.web.server.model.Comment;
import com.natura.web.server.model.Entry;
import com.natura.web.server.model.Flower;
import com.natura.web.server.model.Identification;
import com.natura.web.server.model.SpeciesType;
import com.natura.web.server.model.User;
import com.natura.web.server.service.EntryService;
import com.natura.web.server.service.IdentificationService;
import com.natura.web.server.service.ImageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;
import com.natura.web.server.mapper.UserMapper;
import com.natura.web.server.persistence.database.entity.CommentEntity;
import com.natura.web.server.persistence.database.entity.EntryEntity;
import com.natura.web.server.persistence.database.entity.FlowerEntity;
import com.natura.web.server.persistence.database.entity.IdentificationEntity;
import com.natura.web.server.persistence.database.entity.InsectEntity;
import com.natura.web.server.persistence.database.entity.SpeciesEntity;
import com.natura.web.server.persistence.database.entity.UserEntity;
import com.natura.web.server.persistence.database.repository.CommentRepository;
import com.natura.web.server.persistence.database.repository.EntryRepository;
import com.natura.web.server.persistence.database.repository.IdentificationRepository;
import com.natura.web.server.persistence.database.repository.SpeciesRepository;
import com.natura.web.server.persistence.database.repository.UserRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Disabled
class IdentificationTests {

    @Autowired
    EntryService entryService;

    @Autowired
    ImageService imageService;

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

    @Autowired
    UserMapper mapper;

    @BeforeEach
    void init() {

        String username = "testUser";
        Optional<UserEntity> userOpt = userRepository.findByUsername(username);
        UserEntity user = userOpt.orElse(null);
        if (user == null) {
            // Create an user
            user = new UserEntity();
            user.setUsername(username);
            user.setEmail("test@email.com");
            user.setPassword("pwd");
            user = userRepository.save(user);
        }
        this.userId = user.getId();

        String validatorUsername = "validator";
        Optional<UserEntity> validatorOpt = userRepository.findByUsername(validatorUsername);
        UserEntity validator = validatorOpt.orElse(null);
        if (validator == null) {
            // Create an user
            validator = new UserEntity();
            validator.setUsername(validatorUsername);
            validator.setEmail("validator@email.com");
            validator.setPassword("pwd");
            validator = userRepository.save(validator);
        }
        this.validatorId = validator.getId();

        String speciesName = "commonName";
        Optional<SpeciesEntity> opt = speciesRepository.findByCommonName(speciesName);
        SpeciesEntity species = opt.orElse(null);
        if (species == null) {
            // Create a species
            species = new SpeciesEntity();
            species.setCommonName(speciesName);
            species.setScientificName("scientificName");
            species.setType(SpeciesType.Flower);
            species = speciesRepository.save(species);
        }
        this.speciesId = species.getId();

        // Create an entry
        FlowerEntity entry = new FlowerEntity("testFlower", new Date(), "description", "location");
        entry = entryRepository.save(entry);
        this.entryId = entry.getId();
    }

    @Test
    void createIdentification() {
        EntryEntity entry = entryRepository.findById(this.entryId).orElse(null);
        SpeciesEntity species = speciesRepository.findById(this.speciesId).orElse(null);
        UserEntity user = userRepository.findById(this.userId).orElse(null);

        IdentificationEntity identification = new IdentificationEntity(entry, species, user, new Date());
        identification = identificationRepository.save(identification);

        Assertions.assertNotNull(identification, "Saved identification cannot be null.");
        Assertions.assertNotNull(identification.getId(), "Saved identification id cannot be null.");
    }

    @Test
    void createEntryWithSuggestedIdentification() throws DataNotFoundException, InvalidFileException, IOException {
        Entry newFlower = new Flower("roseFlower", new Date(), "desc", "France");

        File file = new File(getClass().getClassLoader().getResource("images/pexels-photo-133472.jpeg").getFile());
        InputStream is = new FileInputStream(file);
        MultipartFile picture = new MockMultipartFile("flower_picture", is);

        Entry saved = entryService.createEntryAndSuggestedIdentification(newFlower, picture.getOriginalFilename(), picture.getContentType(), picture.getInputStream(), this.userId, this.speciesId);

        Assertions.assertNotNull(saved, "Saved entry cannot be null.");

        List<IdentificationEntity> identifications = identificationRepository.findByIdEntryId(saved.getId());
        Assertions.assertTrue(identifications != null && identifications.size() == 1);
    }

    @Test
    void getIdentificationFromEntryAndSpeciesId() {
        // First create and save data
        // create a species
        SpeciesEntity species = new SpeciesEntity();
        species.setCommonName("Azuré bleu céleste");
        species.setScientificName("Lysandra bellargus");
        species.setType(SpeciesType.Insect);
        species = speciesRepository.save(species);
        // create an entry
        InsectEntity entry = new InsectEntity("testButterfly", new Date(), "description", "location");
        entry = entryRepository.save(entry);
        // create the identification
        UserEntity user = userRepository.findById(this.userId).orElse(null);
        IdentificationEntity identification = new IdentificationEntity(entry, species, user, new Date());
        identificationRepository.save(identification);

        // Retrieve identification by entry id and species id
        Optional<IdentificationEntity> found = identificationRepository.findByIdEntryIdAndIdSpeciesId(entry.getId(), species.getId());
        assertThat(found).isPresent();
    }

    private IdentificationEntity createIdentification(SpeciesType type, String entryName, UserEntity user, String speciesName, boolean isValidated) {
        EntryEntity entry = null;
        if (type == SpeciesType.Flower) {
            entry = new FlowerEntity();
        } else if (type == SpeciesType.Insect) {
            entry = new InsectEntity();
        }

        Assertions.assertNotNull(entry);
        entry.setName(entryName);
        entry.setDate(new Date());
        entry.setValidated(isValidated);
        entry = entryRepository.save(entry);

        SpeciesEntity species = new SpeciesEntity();
        species.setType(type);
        species.setCommonName(speciesName);
        species.setScientificName(speciesName);
        species = speciesRepository.save(species);

        IdentificationEntity identification = new IdentificationEntity(entry, species, user, new Date());
        if (isValidated) {
            UserEntity validator = userRepository.findById(this.validatorId).orElse(null);
            identification.setValidatedBy(validator);
            identification.setValidatedDate(new Date());
        }
        return identificationRepository.save(identification);
    }

    @Test
    void getIdentificationsSuggestedByOneUser() {
        // Create an user
        UserEntity user = new UserEntity("testUser2");
        user.setEmail("test2@email.com");
        user.setPassword("pwd");
        user = userRepository.save(user);

        createIdentification(SpeciesType.Flower, "item1", user, "species_flower1", true);
        createIdentification(SpeciesType.Insect, "item2", user, "species_insect1", true);
        createIdentification(SpeciesType.Flower, "item3", user, "species_flower2", false);

        List<IdentificationEntity> identifications = identificationRepository.findBySuggestedBy(user);
        Assertions.assertTrue(identifications != null && identifications.size() == 3);
    }

    @Test
    void cannotGiveValidatorRightsForTwoValidatedIdentifications() throws DataNotFoundException {
        // Create an user
        String username = "testUser3";
        User user = new User(username);
        user.setEmail("test3@email.com");
        user.setPassword("pwd");
        UserEntity entity = mapper.map(user);
        entity = userRepository.save(entity);

        createIdentification(SpeciesType.Flower, "element1", entity, "flower1", true);
        createIdentification(SpeciesType.Insect, "element2", entity, "insect1", true);
        createIdentification(SpeciesType.Flower, "element3", entity, "flower2", true);
        createIdentification(SpeciesType.Flower, "element4", entity, "flower3", false);

        user = mapper.map(entity);
        identificationService.giveValidatorRights(user);
        entity = mapper.map(user);
        entity = userRepository.findByUsername(username).get();
        Assertions.assertTrue(!entity.isFlowerValidator());
    }

    @Test
    void giveValidatorRightsForThreeFlowerValidatedIdentifications() throws DataNotFoundException {
        // Create an user
        String username = "testUser4";
        User user = new User(username);
        user.setEmail("test4@email.com");
        user.setPassword("pwd");
        UserEntity entity = mapper.map(user);
        entity = userRepository.save(entity);

        createIdentification(SpeciesType.Flower, "el1", entity, "plant1", true);
        createIdentification(SpeciesType.Flower, "el2", entity, "plant2", true);
        createIdentification(SpeciesType.Flower, "el3", entity, "plant3", true);

        user = mapper.map(entity);
        identificationService.giveValidatorRights(user);
        entity = mapper.map(user);
        entity = userRepository.findByUsername(username).get();
        Assertions.assertTrue(entity.isFlowerValidator());
    }

    @Test
    void createAndCommentIdentification() throws DataNotFoundException {
        // Create an user
        UserEntity user = new UserEntity("observer");
        user.setEmail("observer@email.com");
        user.setPassword("pwd");
        user = userRepository.save(user);

        IdentificationEntity created = createIdentification(SpeciesType.Flower, "red_flower", user, "Rosaceae", false);
        Long speciesId = created.getSpecies().getId();
        Long entryId = created.getEntry().getId();

        Comment comment = identificationService.comment(entryId, speciesId, user.getId(), "this is a comment to an identification");

        Optional<IdentificationEntity> identification = identificationRepository.findByIdEntryIdAndIdSpeciesId(entryId, speciesId);
        List<CommentEntity> comments = commentRepository.findByIdentification(identification.get());
        Assertions.assertTrue(comments != null && comments.size() == 1);
    }

    @Test
    void likeIdentification() throws DataNotFoundException {
        // Create an user
        UserEntity user = new UserEntity("liker");
        user.setEmail("liker@email.com");
        user.setPassword("pwd");
        user = userRepository.save(user);

        IdentificationEntity created = createIdentification(SpeciesType.Flower, "blue_flower", user, "Cyanus segetum", false);
        Long speciesId = created.getSpecies().getId();
        Long entryId = created.getEntry().getId();

        Identification liked = identificationService.like(entryId, speciesId, user.getId());

        Optional<IdentificationEntity> identification = identificationRepository.findByIdEntryIdAndIdSpeciesId(entryId, speciesId);
        Set<UserEntity> likes = identification.get().getLikes();
        Assertions.assertTrue(likes != null && likes.size() == 1 && likes.iterator().next().getId().equals(user.getId()));
    }

    @Test
    void dislikeIdentification() throws DataNotFoundException {
        // Create an user
        UserEntity user = new UserEntity("liker2");
        user.setEmail("liker2@email.com");
        user.setPassword("pwd");
        user = userRepository.save(user);

        IdentificationEntity created = createIdentification(SpeciesType.Flower, "white_flower", user, "Orchidaceae", false);
        Long speciesId = created.getSpecies().getId();
        Long entryId = created.getEntry().getId();

        created.getLikes().add(user);
        identificationRepository.save(created);
        Assertions.assertTrue(created.getLikes() != null && created.getLikes().size() == 1 && created.getLikes().iterator().next().getId().equals(user.getId()));

        Identification disliked = identificationService.like(entryId, speciesId, user.getId());

        Optional<IdentificationEntity> identification = identificationRepository.findByIdEntryIdAndIdSpeciesId(entryId, speciesId);
        Set<UserEntity> likes = identification.get().getLikes();
        Assertions.assertTrue(likes != null && likes.size() == 0);
    }

}
