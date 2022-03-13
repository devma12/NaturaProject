package com.natura.web.server.controllers;

import com.natura.web.server.dto.ErrorDto;
import com.natura.web.server.integration.ControllerIntegrationTest;
import com.natura.web.server.model.Flower;
import com.natura.web.server.ports.database.entities.EntryEntity;
import com.natura.web.server.ports.database.entities.FlowerEntity;
import com.natura.web.server.ports.database.entities.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("[TI] Rest request should")
class FlowerControllerIntegrationTest extends ControllerIntegrationTest {

    private static final String FLOWER_URL = "/natura-api/flower";

    @Test
    @WithMockUser
    @DisplayName("get response 201 when creating a flower.")
    void save_flower() throws Exception {
        // Given
        MockMultipartFile mockMultipartFile = new MockMultipartFile("flower-file", "test.txt",
                "text/plain", "test data".getBytes());
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("name", "flowerA");
        requestParams.add("description", "descriptionA");
        requestParams.add("location", "locationA");
        requestParams.add("date", "01/01/2022");
        requestParams.add("species", SPECIES_ID.toString());
        requestParams.add("createdBy", USER_ID.toString());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.multipart(FLOWER_URL + "/new")
                .file("imageFile", mockMultipartFile.getBytes())
                .params(requestParams);

        // When
        MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isCreated()).andReturn();

        // Then
        String response = result.getResponse().getContentAsString();
        Flower entry = objectMapper.readValue(response, Flower.class);
        assertThat(entry.getName()).isEqualTo("flowerA");
        assertThat(entry.getDescription()).isEqualTo("descriptionA");
        assertThat(entry.getLocation()).isEqualTo("locationA");
        assertThat(entry.getDate()).isEqualTo(new Date("01/01/2022"));
        assertThat(entry.getCreatedBy().getUsername()).isEqualTo("testUser");
        assertThat(entry.getImage()).isNotNull();
    }

    @Test
    @WithMockUser
    @DisplayName("get response 200 when getting all flowers.")
    void get_all_flowers() throws Exception {
        // Given
        entryRepository.deleteAll();
        entryRepository.save(new FlowerEntity("flower1", new Date(), "description", "location"));
        entryRepository.save(new FlowerEntity("flower2", new Date(), "desc", "loc"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(FLOWER_URL + "/all");

        // When
        MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();

        // Then
        String response = result.getResponse().getContentAsString();
        Flower[] entries = objectMapper.readValue(response, Flower[].class);
        assertThat(entries).hasSize(2)
                .anyMatch(e -> e.getName().equals("flower1"))
                .anyMatch(e -> e.getName().equals("flower2"));
    }

    @Test
    @WithMockUser
    @DisplayName("get response 200 when getting flower by id.")
    void get_flower_by_id() throws Exception {
        // Given
        entryRepository.deleteAll();
        FlowerEntity entity = entryRepository.save(new FlowerEntity("flower", new Date(), "description", "location"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(FLOWER_URL + "/" + entity.getId());

        // When
        MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();

        // Then
        String response = result.getResponse().getContentAsString();
        Flower entry = objectMapper.readValue(response, Flower.class);
        assertThat(entry.getName()).isEqualTo("flower");
    }

    @Test
    @WithMockUser
    @DisplayName("get response 200 when getting flowers created by specific user.")
    void get_flower_by_creator() throws Exception {
        // Given
        entryRepository.deleteAll();
        Optional<UserEntity> user = userRepository.findById(USER_ID);
        assertThat(user).isPresent();
        EntryEntity saved = new FlowerEntity("flower", new Date(), "description", "location");
        saved.setCreatedBy(user.get());
        EntryEntity entity = entryRepository.save(saved);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(FLOWER_URL + "/" + entity.getId());

        // When
        MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();

        // Then
        String response = result.getResponse().getContentAsString();
        Flower entry = objectMapper.readValue(response, Flower.class);
        assertThat(entry.getName()).isEqualTo("flower");
    }

    @Test
    @WithMockUser
    @DisplayName("get response 404 when getting flower by id that does not exist.")
    void get_flower_by_id_not_found() throws Exception {
        // Given
        entryRepository.deleteAll();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(FLOWER_URL + "/" + 1L);

        // When
        MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isNotFound()).andReturn();

        // Then
        String response = result.getResponse().getContentAsString();
        ErrorDto error = objectMapper.readValue(response, ErrorDto.class);
        assertThat(error.getCode()).isEqualTo(100);
    }
}
