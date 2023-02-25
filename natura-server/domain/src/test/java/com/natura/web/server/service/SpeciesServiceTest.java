package com.natura.web.server.service;

import com.natura.web.server.exception.InvalidDataException;
import com.natura.web.server.model.Phenology;
import com.natura.web.server.model.Species;
import com.natura.web.server.model.SpeciesType;
import com.natura.web.server.provider.SpeciesProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@DisplayName("Species service should")
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

/*
        when(speciesProvider.save(Mockito.any())).then(AdditionalAnswers.returnsFirstArg());
        when(speciesProvider.getSpeciesByScientificName(scientificName)).thenReturn(Optional.of(new Species()));
        when(speciesProvider.getSpeciesByCommonName(commonName)).thenReturn(Optional.of(new Species()));
*/

    }

    @Test
    @DisplayName("raise DuplicateDataException when creating species with existing scientific name")
    void createSpeciesWithDuplicateScientificName() {
        Species species = createDefaultButterfly();
        species.setCommonName("Vanesse de l'ortie");
        when(speciesProvider.getSpeciesByScientificName(scientificName)).thenReturn(Optional.of(new Species()));
        InvalidDataException.DuplicateDataException exception = assertThrows(InvalidDataException.DuplicateDataException.class, () -> speciesService.create(species));
        assertThat(exception.getMessage()).contains("scientific name");
    }

    @Test
    @DisplayName("raise DuplicateDataException when creating species with existing common name")
    void createSpeciesWithDuplicateCommonName() {
        Species species = createDefaultButterfly();
        species.setScientificName("Undetermined");
        when(speciesProvider.getSpeciesByCommonName(commonName)).thenReturn(Optional.of(new Species()));
        InvalidDataException.DuplicateDataException exception = assertThrows(InvalidDataException.DuplicateDataException.class, () -> speciesService.create(species));
        assertThat(exception.getMessage()).contains("common name");
    }

    private static Stream<Arguments> phenologies() {
        return Stream.of(
                Arguments.of(Month.JUNE, Month.OCTOBER),
                Arguments.of(Month.JUNE, Month.AUGUST),
                Arguments.of(Month.MARCH, Month.AUGUST),
                Arguments.of(Month.MARCH, Month.OCTOBER),
                Arguments.of(Month.FEBRUARY, Month.APRIL),
                Arguments.of(Month.SEPTEMBER, Month.NOVEMBER)
        );
    }

    @ParameterizedTest
    @MethodSource("phenologies")
    @DisplayName("raise InvalidDataException when creating species with phenologies overlap")
    void testInvalidPhenologies(Month start, Month end) {
        Species species = createDefaultButterfly();
        species.setScientificName("Undetermined");
        species.setCommonName("Vanesse de l'ortie");
        species.getPhenologies().add(new Phenology(start, end));

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> speciesService.create(species));
        assertThat(exception.getMessage()).isEqualTo("Invalid phenologies: Periods overlap.");
    }
    @Test
    @DisplayName("raise InvalidDataException when creating species with inversed phenology")
    void createSpeciesWithReversedPhenology() {
        Species species = createDefaultButterfly();
        species.setScientificName("Undetermined");
        species.setCommonName("Vanesse de l'ortie");
        species.getPhenologies().add(new Phenology(Month.NOVEMBER, Month.OCTOBER));

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> speciesService.create(species));
        assertThat(exception.getMessage()).isEqualTo("Invalid phenology: Start and end months should be reversed.");
    }

    @Test
    @DisplayName("create species with given valid data")
    void createSpecies() throws InvalidDataException {
        // Given
        Species species = createDefaultButterfly();
        species.setScientificName("Aglais io");
        species.setCommonName("Paon du jour");
        species.getPhenologies().add(new Phenology(Month.OCTOBER, Month.NOVEMBER));
        when(speciesProvider.save(Mockito.any())).then(AdditionalAnswers.returnsFirstArg());

        // When
        Species created = speciesService.create(species);

        // Then
        assertThat(created).isNotNull()
                .isEqualTo(species);
    }

    @Test
    @DisplayName("return all species.")
    void getInsects() {
        // Given
        List<Species> species = Collections.singletonList(new Species());
        when(speciesProvider.getSpecies()).thenReturn(species);

        // When
        List<Species> result = speciesProvider.getSpecies();

        // Then
        assertThat(result).isEqualTo(species);
    }

    @Test
    @DisplayName("return species with given type.")
    void getSpeciesByType() {
        // Given
        List<Species> species = Collections.singletonList(new Species());
        when(speciesProvider.getSpeciesByType(SpeciesType.Insect)).thenReturn(species);

        // When
        List<Species> found = speciesService.getSpeciesByType(SpeciesType.Insect);

        // Then
        assertThat(found).isEqualTo(species);
    }

    private Species createDefaultButterfly() {
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