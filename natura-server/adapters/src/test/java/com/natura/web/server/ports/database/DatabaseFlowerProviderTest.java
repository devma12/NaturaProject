package com.natura.web.server.ports.database;

import com.natura.web.server.mappers.EntryMapper;
import com.natura.web.server.model.Flower;
import com.natura.web.server.ports.database.entities.FlowerEntity;
import com.natura.web.server.ports.database.repo.FlowerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("Database flower provider should")
@ExtendWith(MockitoExtension.class)
class DatabaseFlowerProviderTest {

    private static final Long ENTRY_ID = 1L;

    @InjectMocks
    DatabaseFlowerProvider provider;

    @Mock
    EntryMapper mapper;

    @Mock
    FlowerRepository repository;

    @Test
    @DisplayName("return flower with given id if exists.")
    void getFlowerById() {
        // Given
        Optional<FlowerEntity> entity = Optional.of(new FlowerEntity());
        Flower entry = new Flower();
        when(repository.findById(ENTRY_ID)).thenReturn(entity);
        when(mapper.mapFlowerOptional(entity)).thenReturn(Optional.of(entry));

        // When
        Optional<Flower> result = provider.getFlowerById(ENTRY_ID);

        // Then
        assertThat(result).isPresent().contains(entry);
    }

    @Test
    @DisplayName("return empty if no flower exists with given id.")
    void getFlowerById_null() {
        // Given
        when(repository.findById(ENTRY_ID)).thenReturn(Optional.empty());

        // When
        Optional<Flower> result = provider.getFlowerById(ENTRY_ID);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("return all flowers.")
    void getFlowers() {
        // Given
        List<FlowerEntity> entities = List.of(new FlowerEntity());
        List<Flower> flowers = List.of(new Flower());
        when(repository.findAll()).thenReturn(entities);
        when(mapper.mapFlowerList(entities)).thenReturn(flowers);

        // When
        List<Flower> result = provider.getFlowers();

        // Then
        assertThat(result).isEqualTo(flowers);
    }

    @Test
    @DisplayName("return all flowers created by user with given id.")
    void getFlowersByCreatedBy() {
        // Given
        Long userId = 1L;
        List<FlowerEntity> entities = List.of(new FlowerEntity());
        List<Flower> flowers = List.of(new Flower());
        when(repository.findByCreatedById(userId)).thenReturn(entities);
        when(mapper.mapFlowerList(entities)).thenReturn(flowers);

        // When
        List<Flower> result = provider.getFlowersByCreatedBy(userId);

        // Then
        assertThat(result).isEqualTo(flowers);
    }
}