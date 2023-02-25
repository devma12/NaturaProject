package com.natura.web.server.persistence.database;

import com.natura.web.server.mapper.SpeciesMapper;
import com.natura.web.server.model.Species;
import com.natura.web.server.model.SpeciesType;
import com.natura.web.server.persistence.database.entity.SpeciesEntity;
import com.natura.web.server.persistence.database.repository.SpeciesRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Database species provider should")
@ExtendWith(MockitoExtension.class)
class DatabaseSpeciesProviderTest {

    private static final Long SPECIES_ID = 1L;

    @InjectMocks
    DatabaseSpeciesProvider provider;

    @Mock
    SpeciesMapper mapper;

    @Mock
    SpeciesRepository repository;

    @Test
    @DisplayName("save species.")
    void save() {
        // Given
        SpeciesEntity entity = new SpeciesEntity();
        Species species = new Species();
        Species saved = new Species();
        when(mapper.map(species)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.map(entity)).thenReturn(saved);

        // When
        Species result = provider.save(species);

        // Then
        verify(repository).save(entity);
        assertThat(result).isEqualTo(saved);
    }

    @Test
    @DisplayName("return species with given id if exists.")
    void getSpeciesById() {
        // Given
        Optional<SpeciesEntity> entity = Optional.of(new SpeciesEntity());
        Species species = new Species();
        when(repository.findById(SPECIES_ID)).thenReturn(entity);
        when(mapper.map(entity)).thenReturn(Optional.of(species));

        // When
        Optional<Species> result = provider.getSpeciesById(SPECIES_ID);

        // Then
        assertThat(result).isPresent().contains(species);
    }

    @Test
    @DisplayName("return empty if no species exists with given id.")
    void getSpeciesById_null() {
        // Given
        when(repository.findById(SPECIES_ID)).thenReturn(Optional.empty());

        // When
        Optional<Species> result = provider.getSpeciesById(SPECIES_ID);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("return species with given scientific name if exists.")
    void getSpeciesByScientificName() {
        // Given
        String scientificName = "scientificName";
        Optional<SpeciesEntity> entity = Optional.of(new SpeciesEntity());
        Species species = new Species();
        when(repository.findByScientificName(scientificName)).thenReturn(entity);
        when(mapper.map(entity)).thenReturn(Optional.of(species));

        // When
        Optional<Species> result = provider.getSpeciesByScientificName(scientificName);

        // Then
        assertThat(result).isPresent().contains(species);
    }

    @Test
    @DisplayName("return empty if no species exists with given scientific name.")
    void getSpeciesByScientificName_null() {
        // Given
        String scientificName = "scientificName";
        when(repository.findByScientificName(scientificName)).thenReturn(Optional.empty());

        // When
        Optional<Species> result = provider.getSpeciesByScientificName(scientificName);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("return species with given common name if exists.")
    void getSpeciesByCommonName() {
        // Given
        String commonName = "commonName";
        Optional<SpeciesEntity> entity = Optional.of(new SpeciesEntity());
        Species species = new Species();
        when(repository.findByCommonName(commonName)).thenReturn(entity);
        when(mapper.map(entity)).thenReturn(Optional.of(species));

        // When
        Optional<Species> result = provider.getSpeciesByCommonName(commonName);

        // Then
        assertThat(result).isPresent().contains(species);
    }

    @Test
    @DisplayName("return empty if no species exists with given common name.")
    void getSpeciesByCommonName_null() {
        // Given
        String commonName = "commonName";
        when(repository.findByCommonName(commonName)).thenReturn(Optional.empty());

        // When
        Optional<Species> result = provider.getSpeciesByCommonName(commonName);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("return all species.")
    void getSpecies() {
        // Given
        List<SpeciesEntity> entities = List.of(new SpeciesEntity());
        List<Species> species = List.of(new Species());
        when(repository.findAll()).thenReturn(entities);
        when(mapper.map(entities)).thenReturn(species);

        // When
        List<Species> result = provider.getSpecies();

        // Then
        assertThat(result).isEqualTo(species);
    }

    @Test
    @DisplayName("return species with given type.")
    void getSpeciesByType() {
        // Given
        List<SpeciesEntity> flowerSpeciesEntities = List.of(new SpeciesEntity());
        List<Species> flowerSpecies = List.of(new Species());
        when(repository.findByType(SpeciesType.Flower)).thenReturn(flowerSpeciesEntities);
        when(mapper.map(flowerSpeciesEntities)).thenReturn(flowerSpecies);

        List<SpeciesEntity> insectSpeciesEntities = List.of(new SpeciesEntity());
        List<Species> insectSpecies = List.of(new Species());
        when(repository.findByType(SpeciesType.Insect)).thenReturn(insectSpeciesEntities);
        when(mapper.map(insectSpeciesEntities)).thenReturn(insectSpecies);

        // When
        List<Species> resultForFlowerType = provider.getSpeciesByType(SpeciesType.Flower);

        // Then
        assertThat(resultForFlowerType).isEqualTo(flowerSpecies);

        // When
        List<Species> resultForInsectType = provider.getSpeciesByType(SpeciesType.Insect);

        // Then
        assertThat(resultForInsectType).isEqualTo(insectSpecies);
    }
}