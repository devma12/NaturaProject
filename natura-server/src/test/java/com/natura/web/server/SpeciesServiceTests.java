package com.natura.web.server;

import com.natura.web.server.entities.*;
import com.natura.web.server.exceptions.InvalidDataException;
import com.natura.web.server.repo.SpeciesRepository;
import com.natura.web.server.services.SpeciesService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.Month;

@SpringBootTest
@ActiveProfiles("test")
public class SpeciesServiceTests {

    @Autowired
    SpeciesService speciesService;

    @MockBean
    SpeciesRepository speciesRepository;

    private static String scientificName = "aglais urticae";
    private static String commonName = "petite tortue";

    @BeforeEach
    void init() {

        Mockito.lenient().when(speciesRepository.save(Mockito.any())).then(AdditionalAnswers.returnsFirstArg());
        Mockito.lenient().when(speciesRepository.findByScientificName(scientificName)).thenReturn(new Species());
        Mockito.lenient().when(speciesRepository.findByCommonName(commonName)).thenReturn(new Species());

    }


    @Test
    void createSpeciesWithDuplicateScientificName() {

        Species species = SpeciesTests.createDefaultButterfly();
        species.setCommonName("Vanesse de l'ortie");

        Assertions.assertThrows(InvalidDataException.DuplicateDataException.class, () -> {
            speciesService.create(species);
        });

    }

    @Test
    void createSpeciesWithDuplicateCommonName() {

        Species species = SpeciesTests.createDefaultButterfly();
        species.setScientificName("Undetermined");

        Assertions.assertThrows(InvalidDataException.DuplicateDataException.class, () -> {
            speciesService.create(species);
        });

    }

    private void testInvalidPhenologies(Month start, Month end) {
        Species species = SpeciesTests.createDefaultButterfly();
        species.setScientificName("Undetermined");
        species.setCommonName("Vanesse de l'ortie");

        species.getPhenologies().add(new Phenology(start, end));

        Assertions.assertThrows(InvalidDataException.class, () -> {
            speciesService.create(species);
        });
    }

    @Test
    void createSpeciesWithPhenologiesOverlap1() {
        testInvalidPhenologies(Month.JUNE, Month.OCTOBER);
    }

    @Test
    void createSpeciesWithPhenologiesOverlap2() {
        testInvalidPhenologies(Month.JUNE, Month.AUGUST);
    }

    @Test
    void createSpeciesWithPhenologiesOverlap3() {
        testInvalidPhenologies(Month.MARCH, Month.AUGUST);
    }

    @Test
    void createSpeciesWithPhenologiesOverlap4() {
        testInvalidPhenologies(Month.MARCH, Month.OCTOBER);
    }

    @Test
    void createSpeciesWithPhenologiesOverlap5() {
        testInvalidPhenologies(Month.FEBRUARY, Month.APRIL);
    }

    @Test
    void createSpeciesWithPhenologiesOverlap6() {
        testInvalidPhenologies(Month.SEPTEMBER, Month.NOVEMBER);
    }

    @Test
    void createSpeciesWithReversedPhenology() { testInvalidPhenologies(Month.NOVEMBER, Month.OCTOBER); }

    @Test
    void createSpecies() throws InvalidDataException {

        Species species = SpeciesTests.createDefaultButterfly();
        species.setScientificName("Aglais io");
        species.setCommonName("Paon du jour");

        species.getPhenologies().add(new Phenology(Month.OCTOBER, Month.NOVEMBER));

        speciesService.create(species);

    }

}
