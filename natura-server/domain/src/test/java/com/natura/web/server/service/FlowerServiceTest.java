package com.natura.web.server.service;

import com.natura.web.server.model.Flower;
import com.natura.web.server.provider.FlowerProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("Flower service should")
@ExtendWith(MockitoExtension.class)
class FlowerServiceTest {

    private static final Long ENTRY_ID = 1L;
    private static final Long USER_ID = 1L;

    @InjectMocks
    FlowerService flowerService;

    @Mock
    FlowerProvider flowerProvider;

    @Test
    @DisplayName("return flower for given id.")
    void getFlowerById() {
        // Given
        Flower flower = new Flower();
        when(flowerProvider.getFlowerById(1L)).thenReturn(Optional.of(flower));

        // When
        Optional<Flower> found = flowerService.getFlowerById(ENTRY_ID);

        // Then
        assertThat(found).isPresent()
                .hasValue(flower);
    }

    @Test
    @DisplayName("return all flowers.")
    void getFlowers() {
        // Given
        List<Flower> flowers = Collections.singletonList(new Flower());
        when(flowerProvider.getFlowers()).thenReturn(flowers);

        // When
        List<Flower> flowerList = flowerService.getFlowers();

        // Then
        assertThat(flowerList).isEqualTo(flowers);
    }

    @Test
    @DisplayName("return flowers created by given user id.")
    void getFlowersByCreator() {
        // Given
        List<Flower> flowers = Collections.singletonList(new Flower());
        when(flowerProvider.getFlowersByCreatedBy(USER_ID)).thenReturn(flowers);

        // When
        List<Flower> found = flowerService.getFlowersByCreator(USER_ID);

        // Then
        assertThat(found).isEqualTo(flowers);
    }
}