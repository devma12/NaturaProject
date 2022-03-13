package com.natura.web.server.ports.database;

import com.natura.web.server.ports.database.entities.EntryEntity;
import com.natura.web.server.ports.database.entities.InsectEntity;
import com.natura.web.server.mappers.EntryMapper;
import com.natura.web.server.model.Entry;
import com.natura.web.server.model.Insect;
import com.natura.web.server.ports.database.repo.EntryRepository;
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

@DisplayName("Database entry provider should")
@ExtendWith(MockitoExtension.class)
class DatabaseEntryProviderTest {

    private static final Long ENTRY_ID = 1L;

    @InjectMocks
    DatabaseEntryProvider provider;

    @Mock
    EntryMapper mapper;

    @Mock
    EntryRepository repository;

    @Test
    @DisplayName("save entry.")
    void save() {
        // Given
        EntryEntity entity = new InsectEntity();
        Entry entry = new Insect();
        Entry saved = new Insect();
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.mapEntry(entry)).thenReturn(entity);
        when(mapper.mapEntryEntity(entity)).thenReturn(saved);

        // When
        Entry result = provider.save(entry);

        // Then
        verify(repository).save(entity);
        assertThat(result).isEqualTo(saved);
    }

    @Test
    @DisplayName("return entry with given id if exists.")
    void getEntryById() {
        // Given
        Optional<EntryEntity> entity = Optional.of(new InsectEntity());
        Entry entry = new Insect();
        when(repository.findById(ENTRY_ID)).thenReturn(entity);
        when(mapper.map(entity)).thenReturn(Optional.of(entry));

        // When
        Optional<Entry> result = provider.getEntryById(ENTRY_ID);

        // Then
        assertThat(result).isPresent().contains(entry);
    }

    @Test
    @DisplayName("return empty if no entry exists with given id.")
    void getEntryById_null() {
        // Given
        when(repository.findById(ENTRY_ID)).thenReturn(Optional.empty());

        // When
        Optional<Entry> result = provider.getEntryById(ENTRY_ID);

        // Then
        assertThat(result).isEmpty();
    }
}