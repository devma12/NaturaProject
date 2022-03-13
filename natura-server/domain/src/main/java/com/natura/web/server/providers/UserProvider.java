package com.natura.web.server.providers;

import com.natura.web.server.model.User;

import java.util.Optional;

public interface UserProvider {
    Optional<User> getUserById(Long userId);

    User save(User user);

    Optional<User> getUserByEmail(String email);

    Optional<User> getUserByUsername(String username);
}
