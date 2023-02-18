package com.natura.web.server.service;

import com.natura.web.server.exception.InvalidDataException;
import com.natura.web.server.model.Phenology;
import com.natura.web.server.model.Species;
import com.natura.web.server.model.SpeciesType;
import com.natura.web.server.provider.SpeciesProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class SpeciesServiceTest {

    @InjectMocks
    SpeciesService speciesService;

    @Mock
    SpeciesProvider speciesProvider;

    private static final String scientificName = "aglais urticae";
    private static final String commonName = "petite tortue";

    @BeforeEach
    void init() {

        Mockito.lenient().when(speciesProvider.save(Mockito.any())).then(AdditionalAnswers.returnsFirstArg());
        Mockito.lenient().when(speciesProvider.getSpeciesByScientificName(scientificName)).thenReturn(Optional.of(new Species()));
        Mockito.lenient().when(speciesProvider.getSpeciesByCommonName(commonName)).thenReturn(Optional.of(new Species()));

    }

    @Test
    void createSpeciesWithDuplicateScientificName() {
        Species species = createDefaultButterfly();
        species.setCommonName("Vanesse de l'ortie");

        Assertions.assertThrows(InvalidDataException.DuplicateDataException.class, () -> speciesService.create(species));

    }

    @Test
    void createSpeciesWithDuplicateCommonName() {
        Species species = createDefaultButterfly();
        species.setScientificName("Undetermined");

        Assertions.assertThrows(InvalidDataException.DuplicateDataException.class, () -> speciesService.create(species));

    }

    private void testInvalidPhenologies(Month start, Month end) {
        Species species = createDefaultButterfly();
        species.setScientificName("Undetermined");
        species.setCommonName("Vanesse de l'ortie");

        species.getPhenologies().add(new Phenology(start, end));

        Assertions.assertThrows(InvalidDataException.class, () -> speciesService.create(species));
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
    void createSpeciesWithReversedPhenology() {
        testInvalidPhenologies(Month.NOVEMBER, Month.OCTOBER);
    }

    @Test
    void createSpecies() throws InvalidDataException {
        Species species = createDefaultButterfly();
        species.setScientificName("Aglais io");
        species.setCommonName("Paon du jour");

        species.getPhenologies().add(new Phenology(Month.OCTOBER, Month.NOVEMBER));

        speciesService.create(species);

    }

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
}