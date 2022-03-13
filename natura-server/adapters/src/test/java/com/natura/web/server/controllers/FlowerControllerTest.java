package com.natura.web.server.controllers;

import com.natura.web.server.exceptions.DataNotFoundException;
import com.natura.web.server.exceptions.InvalidDataException.InvalidFileException;
import com.natura.web.server.model.Entry;
import com.natura.web.server.model.Flower;
import com.natura.web.server.services.EntryService;
import com.natura.web.server.services.FlowerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Flower controller should")
@ExtendWith(MockitoExtension.class)
class FlowerControllerTest {

    @InjectMocks
    FlowerController controller;

    @Mock
    EntryService entryService;

    @Mock
    FlowerService flowerService;

    @DisplayName("call entry service to create flower.")
    @Test
    void create() throws DataNotFoundException, InvalidFileException, IOException {
        // Given
        MultipartFile file = new MockMultipartFile("mocked", "mocked", "png", new byte[10]);
        when(entryService.createEntryAndSuggestedIdentification(any(), any(), any(),  any(), anyLong(), anyLong())).thenReturn(new Flower());

        // When
        controller.create(file, "flower", new Date(), "description", "location", "1", "2");

        // Then
        ArgumentCaptor<Entry> captor = ArgumentCaptor.forClass(Entry.class);
        verify(entryService).createEntryAndSuggestedIdentification(captor.capture(), eq(file.getOriginalFilename()), eq(file.getContentType()), any(InputStream.class), eq(2L), eq(1L));
        Entry entry = captor.getValue();
        assertThat(entry.getName()).isEqualTo("flower");
        assertThat(entry.getDescription()).isEqualTo("description");
        assertThat(entry.getLocation()).isEqualTo("location");
    }

    @DisplayName("throw error if species id number is invalid.")
    @Test
    void create_species_id_error() {
        // Given
        MultipartFile file = new MockMultipartFile("mocked", new byte[10]);

        // When
        Throwable exception = catchThrowable(() -> controller.create(file, "flower", new Date(), "description", "location", "A", "2"));

        // Then
        assertThat(exception).isInstanceOf(ResponseStatusException.class);
        assertThat(((ResponseStatusException) exception).getStatusCode().value()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(((ResponseStatusException) exception).getReason()).isEqualTo("Number format is invalid for species id : A.");
    }

    @DisplayName("throw error if service throws a ServerException.")
    @Test
    void create_server_exception() throws DataNotFoundException, InvalidFileException {
        // Given
        MultipartFile file = new MockMultipartFile("mocked", new byte[10]);
        Exception ex = new DataNotFoundException("test");
        when(entryService.createEntryAndSuggestedIdentification(any(), any(), any(), any(), anyLong(), anyLong())).thenThrow(ex);

        // When
        Throwable exception = catchThrowable(() -> controller.create(file, "flower", new Date(), "description", "location", "1", "2"));

        // Then
        assertThat(exception).isEqualTo(ex);
    }

    @DisplayName("call flower service to get all flowers.")
    @Test
    void getAllFlowers() {
        // Given
        List<Flower> flowers = List.of(new Flower());
        when(flowerService.getFlowers()).thenReturn(flowers);

        // When
        List<Flower> result = controller.getAllFlowers();

        // Then
        assertThat(result).isEqualTo(flowers);
    }

    @DisplayName("call flower service to get flower by id.")
    @Test
    void getById() throws DataNotFoundException {
        // Given
        Flower flower = new Flower();
        when(flowerService.getFlowerById(1L)).thenReturn(Optional.of(flower));

        // When
        Flower result = controller.getById("1");

        // Then
        assertThat(result).isEqualTo(flower);
    }

    @DisplayName("call flower service to get flowers created by user.")
    @Test
    void getByCreator() {
        // Given
        List<Flower> flowers = List.of(new Flower());
        when(flowerService.getFlowersByCreator(1L)).thenReturn(flowers);

        // When
        List<Flower> result = controller.getByCreator("1");

        // Then
        assertThat(result).isEqualTo(flowers);
    }
}