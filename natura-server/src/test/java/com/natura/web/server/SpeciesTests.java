package com.natura.web.server;

import com.natura.web.server.entities.Phenology;
import com.natura.web.server.entities.Species;
import com.natura.web.server.repo.SpeciesRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Month;
import java.util.HashSet;
import java.util.Set;

@SpringBootTest
@ActiveProfiles("test")
public class SpeciesTests {

    @Autowired
    SpeciesRepository speciesRepository;

    @Test
    void createSpecies() {
        Species species = new Species();
        species.setCommonName("Petite tortue");
        species.setScientificName("Aglais urticae");
        species.setType(Species.Type.Insect);
        species.setOrder("Lepidoptera");
        species.setFamily("Nymphalidae");
        Set<Phenology> phenologies = new HashSet<>();
        phenologies.add(new Phenology(Month.MAY, Month.SEPTEMBER));
        species.setPhenologies(phenologies);
        species.setHabitatType("rural");

        Species saved = speciesRepository.save(species);

        Assertions.assertNotNull(saved, "Saved species cannot be null.");
        Assertions.assertTrue(saved.getPhenologies().size() > 0, "Species phenologies cannot be empty.");
        Assertions.assertNotNull(saved.getPhenologies().iterator().next(), "Species phenology cannot be null.");
        Assertions.assertNotNull(saved.getPhenologies().iterator().next().getId(), "Saved species phenology must be automatically saved with an automatically generated id.");

    }
}
