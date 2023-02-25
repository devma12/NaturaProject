package com.natura.web.server.persistence.database;

import com.natura.web.server.model.Insect;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.natura.web.server.mapper.EntryMapper;
import com.natura.web.server.persistence.database.entity.InsectEntity;
import com.natura.web.server.persistence.database.repository.InsectRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("Database insect provider should")
@ExtendWith(MockitoExtension.class)
class DatabaseInsectProviderTest {

    private static final Long ENTRY_ID = 1L;

    @InjectMocks
    DatabaseInsectProvider provider;

    @Mock
    EntryMapper mapper;

    @Mock
    InsectRepository repository;

    @Test
    @DisplayName("return insect with given id if exists.")
    void getInsectById() {
        // Given
        Optional<InsectEntity> entity = Optional.of(new InsectEntity());
        Insect entry = new Insect();
        when(repository.findById(ENTRY_ID)).thenReturn(entity);
        when(mapper.mapInsectOptional(entity)).thenReturn(Optional.of(entry));

        // When
        Optional<Insect> result = provider.getInsectById(ENTRY_ID);

        // Then
        assertThat(result).isPresent().contains(entry);
    }

    @Test
    @DisplayName("return empty if no insect exists with given id.")
    void getInsectById_null() {
        // Given
        when(repository.findById(ENTRY_ID)).thenReturn(Optional.empty());

        // When
        Optional<Insect> result = provider.getInsectById(ENTRY_ID);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("return all insects.")
    void getInsects() {
        // Given
        List<InsectEntity> entities = List.of(new InsectEntity());
        List<Insect> insects = List.of(new Insect());
        when(repository.findAll()).thenReturn(entities);
        when(mapper.mapInsectList(entities)).thenReturn(insects);

        // When
        List<Insect> result = provider.getInsects();

        // Then
        assertThat(result).isEqualTo(insects);
    }

    @Test
    @DisplayName("return all insects created by user with given id.")
    void getInsectsByCreatedBy() {
        // Given
        Long userId = 1L;
        List<InsectEntity> entities = List.of(new InsectEntity());
        List<Insect> insects = List.of(new Insect());
        when(repository.findByCreatedById(userId)).thenReturn(entities);
        when(mapper.mapInsectList(entities)).thenReturn(insects);

        // When
        List<Insect> result = provider.getInsectsByCreatedBy(userId);

        // Then
        assertThat(result).isEqualTo(insects);
    }
}