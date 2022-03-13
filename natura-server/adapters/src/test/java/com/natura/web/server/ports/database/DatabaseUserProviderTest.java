package com.natura.web.server.ports.database;

import com.natura.web.server.ports.database.entities.UserEntity;
import com.natura.web.server.mappers.UserMapper;
import com.natura.web.server.model.User;
import com.natura.web.server.ports.database.repo.UserRepository;
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

@DisplayName("Database user provider should")
@ExtendWith(MockitoExtension.class)
class DatabaseUserProviderTest {

    private static final Long USER_ID = 1L;

    @InjectMocks
    DatabaseUserProvider provider;

    @Mock
    UserMapper mapper;

    @Mock
    UserRepository repository;

    @Test
    @DisplayName("save user.")
    void save() {
        // Given
        UserEntity entity = new UserEntity();
        User user = new User();
        User saved = new User();
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.map(user)).thenReturn(entity);
        when(mapper.map(entity)).thenReturn(saved);

        // When
        User result = provider.save(user);

        // Then
        verify(repository).save(entity);
        assertThat(result).isEqualTo(saved);
    }

    @Test
    @DisplayName("return user with given id if exists.")
    void getUserById() {
        // Given
        Optional<UserEntity> entity = Optional.of(new UserEntity());
        User user = new User();
        when(repository.findById(USER_ID)).thenReturn(entity);
        when(mapper.map(entity)).thenReturn(Optional.of(user));

        // When
        Optional<User> result = provider.getUserById(USER_ID);

        // Then
        assertThat(result).isPresent().contains(user);
    }

    @Test
    @DisplayName("return empty if no user exists with given id.")
    void getUserById_null() {
        // Given
        when(repository.findById(USER_ID)).thenReturn(Optional.empty());

        // When
        Optional<User> result = provider.getUserById(USER_ID);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("return user with given username if exists.")
    void getUserByUsername() {
        // Given
        String username = "username";
        Optional<UserEntity> entity = Optional.of(new UserEntity());
        User user = new User();
        when(repository.findByUsername(username)).thenReturn(entity);
        when(mapper.map(entity)).thenReturn(Optional.of(user));

        // When
        Optional<User> result = provider.getUserByUsername(username);

        // Then
        assertThat(result).isPresent().contains(user);
    }

    @Test
    @DisplayName("return empty if no user exists with given username.")
    void getUserByUsername_null() {
        // Given
        String username = "username";
        when(repository.findByUsername(username)).thenReturn(Optional.empty());

        // When
        Optional<User> result = provider.getUserByUsername(username);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("return user with given email if exists.")
    void getUserByEmail() {
        // Given
        String email = "email";
        Optional<UserEntity> entity = Optional.of(new UserEntity());
        User user = new User();
        when(repository.findByEmail(email)).thenReturn(entity);
        when(mapper.map(entity)).thenReturn(Optional.of(user));

        // When
        Optional<User> result = provider.getUserByEmail(email);

        // Then
        assertThat(result).isPresent().contains(user);
    }

    @Test
    @DisplayName("return empty if no user exists with given email.")
    void getUserByEmail_null() {
        // Given
        String email = "email";
        when(repository.findByEmail(email)).thenReturn(Optional.empty());

        // When
        Optional<User> result = provider.getUserByEmail(email);

        // Then
        assertThat(result).isEmpty();
    }
}