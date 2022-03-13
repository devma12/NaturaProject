package com.natura.web.server.integration.db;

import com.natura.web.server.ports.database.entities.SpeciesEntity;
import com.natura.web.server.mappers.SpeciesMapper;
import com.natura.web.server.model.Phenology;
import com.natura.web.server.model.Species;
import com.natura.web.server.model.SpeciesType;
import com.natura.web.server.ports.database.repo.SpeciesRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Disabled
public class SpeciesTests {

    @Autowired
    SpeciesRepository speciesRepository;

    @Autowired
    SpeciesMapper mapper;

    static public Species createDefaultButterfly() {
        Species species = new Species();
        species.setCommonName("Petite tortue");
        species.setScientificName("Aglais urticae");
        species.setType(SpeciesType.Insect);
        species.setOrder("Lepidoptera");
        species.setFamily("Nymphalidae");
        List<Phenology> phenologies = new ArrayList<>();
        phenologies.add(new Phenology(Month.APRIL, Month.SEPTEMBER));
        species.setPhenologies(phenologies);
        species.setHabitatType("rural");
        return species;
    }

    @Test
    void createSpecies() {
        Species species = createDefaultButterfly();
        SpeciesEntity entity = mapper.map(species);
        SpeciesEntity saved = speciesRepository.save(entity);

        Assertions.assertNotNull(saved, "Saved species cannot be null.");
        Assertions.assertTrue(saved.getPhenologies().size() > 0, "Species phenologies cannot be empty.");
        Assertions.assertNotNull(saved.getPhenologies().iterator().next(), "Species phenology cannot be null.");
        Assertions.assertNotNull(saved.getPhenologies().iterator().next().getId(), "Saved species phenology must be automatically saved with an automatically generated id.");

    }
}
