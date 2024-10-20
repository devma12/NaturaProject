package com.natura.web.server.unit.services;

import static com.natura.web.server.TestUtils.createDefaultButterfly;

import com.natura.web.server.entities.Phenology;
import com.natura.web.server.entities.Species;
import com.natura.web.server.exceptions.InvalidDataException;
import com.natura.web.server.repository.SpeciesRepository;
import com.natura.web.server.services.SpeciesService;
import java.time.Month;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class SpeciesServiceTest {

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

    Species species = createDefaultButterfly();
    species.setCommonName("Vanesse de l'ortie");

    Assertions.assertThrows(InvalidDataException.DuplicateDataException.class, () -> {
      speciesService.create(species);
    });

  }

  @Test
  void createSpeciesWithDuplicateCommonName() {

    Species species = createDefaultButterfly();
    species.setScientificName("Undetermined");

    Assertions.assertThrows(InvalidDataException.DuplicateDataException.class, () -> {
      speciesService.create(species);
    });

  }

  private void testInvalidPhenologies(Month start, Month end) {
    Species species = createDefaultButterfly();
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

}
