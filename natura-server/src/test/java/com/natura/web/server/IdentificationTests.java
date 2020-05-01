package com.natura.web.server;

import com.natura.web.server.entities.*;
import com.natura.web.server.exceptions.DataNotFoundException;
import com.natura.web.server.repo.EntryRepository;
import com.natura.web.server.repo.IdentificationRepository;
import com.natura.web.server.repo.SpeciesRepository;
import com.natura.web.server.repo.UserRepository;
import com.natura.web.server.services.EntryService;
import com.natura.web.server.services.ImageService;
import com.natura.web.server.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class IdentificationTests {

    @Autowired
    EntryService entryService;

    @Autowired
    ImageService imageService;

    @Autowired
    UserService userService;

    @Autowired
    EntryRepository entryRepository;

    @Autowired
    SpeciesRepository speciesRepository;

    @Autowired
    IdentificationRepository identificationRepository;

    @Autowired
    UserRepository userRepository;

    Long userId = -1L;

    Long speciesId = -1L;

    Long entryId = -1L;

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
            this.userId = user.getId();
        }
        this.userId = user.getId();

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
        Flower entry = new Flower("testFlower", "description", "location");
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
        Entry newFlower = new Flower("roseFlower", "desc", "France");

        File file = new File(getClass().getClassLoader().getResource("images/pexels-photo-133472.jpeg").getFile());
        InputStream is = new FileInputStream(file);
        MultipartFile picture = new MockMultipartFile("flower_picture", is);

        Entry saved = entryService.create(newFlower, picture, this.userId, this.speciesId);

        Assertions.assertNotNull(saved, "Saved entry cannot be null.");

        List<Identification> identifications = identificationRepository.findByIdEntryId(saved.getId());
        Assertions.assertTrue(identifications != null && identifications.size() == 1);
    }
}
