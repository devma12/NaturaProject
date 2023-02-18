package com.natura.web.server.service;

import com.natura.web.server.model.Insect;
import com.natura.web.server.provider.InsectProvider;
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

@DisplayName("Insect service should")
@ExtendWith(MockitoExtension.class)
class InsectServiceTest {

    private static final Long ENTRY_ID = 1L;
    private static final Long USER_ID = 1L;

    @InjectMocks
    InsectService insectService;

    @Mock
    InsectProvider insectProvider;

    @Test
    @DisplayName("return insect for given id.")
    void getInsectById() {
        // Given
        Insect insect = new Insect();
        when(insectProvider.getInsectById(1L)).thenReturn(Optional.of(insect));

        // When
        Optional<Insect> found = insectService.getInsectById(ENTRY_ID);

        // Then
        assertThat(found).isPresent()
                .hasValue(insect);
    }

    @Test
    @DisplayName("return all insects.")
    void getInsects() {
        // Given
        List<Insect> insects = Collections.singletonList(new Insect());
        when(insectProvider.getInsects()).thenReturn(insects);

        // When
        List<Insect> insectList = insectService.getInsects();

        // Then
        assertThat(insectList).isEqualTo(insects);
    }

    @Test
    @DisplayName("return insects created by given user id.")
    void getInsectByCreator() {
        // Given
        List<Insect> insects = Collections.singletonList(new Insect());
        when(insectProvider.getInsectsByCreatedBy(USER_ID)).thenReturn(insects);

        // When
        List<Insect> found = insectService.getInsectsByCreator(USER_ID);

        // Then
        assertThat(found).isEqualTo(insects);
    }
}