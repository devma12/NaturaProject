package com.natura.web.server.ports.database;

import com.natura.web.server.ports.database.entities.SpeciesEntity;
import com.natura.web.server.mappers.SpeciesMapper;
import com.natura.web.server.model.Species;
import com.natura.web.server.ports.database.repo.SpeciesRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.map(species)).thenReturn(entity);
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
}