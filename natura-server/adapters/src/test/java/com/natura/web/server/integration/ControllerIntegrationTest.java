package com.natura.web.server.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.natura.web.server.configuration.IntegrationTestConfig;
import com.natura.web.server.ports.database.entities.FlowerEntity;
import com.natura.web.server.ports.database.entities.SpeciesEntity;
import com.natura.web.server.ports.database.entities.UserEntity;
import com.natura.web.server.ports.database.repo.EntryRepository;
import com.natura.web.server.ports.database.repo.IdentificationRepository;
import com.natura.web.server.ports.database.repo.ImageRepository;
import com.natura.web.server.ports.database.repo.SpeciesRepository;
import com.natura.web.server.ports.database.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

@SpringBootTest(classes = {IntegrationTestConfig.class})
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public abstract class ControllerIntegrationTest {

    protected static Long USER_ID;
    protected static Long VALIDATOR_ID;
    protected static Long SPECIES_ID;
    protected static Long ENTRY_ID;

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected SpeciesRepository speciesRepository;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected EntryRepository entryRepository;
    @Autowired
    protected ImageRepository imageRepository;
    @Autowired
    protected IdentificationRepository identificationRepository;

    @BeforeEach
    void init() {
        clearData();
        insertDataSet();
    }

    private void insertDataSet() {
        // Create an user
        UserEntity user = new UserEntity();
        user.setUsername("testUser");
        user.setEmail("test@email.com");
        user.setPassword("pwd");
        user = userRepository.save(user);

        USER_ID = user.getId();


        // Create an user
        UserEntity validator = new UserEntity();
        validator.setUsername("validator");
        validator.setEmail("validator@email.com");
        validator.setPassword("pwd");
        validator = userRepository.save(validator);
        VALIDATOR_ID = validator.getId();

        // Create a species
        SpeciesEntity species = new SpeciesEntity();
        species.setCommonName("commonName");
        species.setScientificName("scientificName");
        species.setType(SpeciesEntity.Type.Flower);
        species = speciesRepository.save(species);
        SPECIES_ID = species.getId();

        // Create an entry
        FlowerEntity entry = new FlowerEntity("testFlower", new Date(), "description", "location");
        entry = entryRepository.save(entry);
        ENTRY_ID = entry.getId();
    }

    private void clearData() {
        this.identificationRepository.deleteAll();
        this.entryRepository.deleteAll();
        this.imageRepository.deleteAll();
        this.speciesRepository.deleteAll();
        this.userRepository.deleteAll();
    }
}
