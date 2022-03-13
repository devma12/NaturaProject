package com.natura.web.server.ports.database;

import com.natura.web.server.ports.database.entities.ImageEntity;
import com.natura.web.server.mappers.ImageMapper;
import com.natura.web.server.model.Image;
import com.natura.web.server.ports.database.repo.ImageRepository;
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

@DisplayName("Database image provider should")
@ExtendWith(MockitoExtension.class)
class DatabaseImageProviderTest {

    private static final Long IMAGE_ID = 1L;

    @InjectMocks
    DatabaseImageProvider provider;

    @Mock
    ImageMapper mapper;

    @Mock
    ImageRepository repository;

    @Test
    @DisplayName("save image.")
    void save() {
        // Given
        ImageEntity entity = new ImageEntity();
        Image image = new Image();
        Image saved = new Image();
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.map(image)).thenReturn(entity);
        when(mapper.map(entity)).thenReturn(saved);

        // When
        Image result = provider.save(image);

        // Then
        verify(repository).save(entity);
        assertThat(result).isEqualTo(saved);
    }

    @Test
    @DisplayName("return image with given id if exists.")
    void getImageById() {
        // Given
        Optional<ImageEntity> entity = Optional.of(new ImageEntity());
        Image image = new Image();
        when(repository.findById(IMAGE_ID)).thenReturn(entity);
        when(mapper.map(entity)).thenReturn(Optional.of(image));

        // When
        Optional<Image> result = provider.getImageById(IMAGE_ID);

        // Then
        assertThat(result).isPresent().contains(image);
    }

    @Test
    @DisplayName("return empty if no image exists with given id.")
    void getImageById_null() {
        // Given
        when(repository.findById(IMAGE_ID)).thenReturn(Optional.empty());

        // When
        Optional<Image> result = provider.getImageById(IMAGE_ID);

        // Then
        assertThat(result).isEmpty();
    }
}