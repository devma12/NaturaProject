package com.natura.web.server.integration.db;

import static com.natura.web.server.TestUtils.createDefaultButterfly;

import com.natura.web.server.entities.Species;
import com.natura.web.server.repository.SpeciesRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class SpeciesTest {

  @Autowired
  SpeciesRepository speciesRepository;

  @Test
  void createSpecies() {
    Species species = createDefaultButterfly();

    Species saved = speciesRepository.save(species);

    Assertions.assertNotNull(saved, "Saved species cannot be null.");
    Assertions.assertTrue(saved.getPhenologies().size() > 0, "Species phenologies cannot be empty.");
    Assertions.assertNotNull(saved.getPhenologies().iterator().next(), "Species phenology cannot be null.");
    Assertions.assertNotNull(saved.getPhenologies().iterator().next().getId(),
        "Saved species phenology must be automatically saved with an automatically generated id.");

  }
}
