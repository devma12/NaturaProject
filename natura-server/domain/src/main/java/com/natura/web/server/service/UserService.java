package com.natura.web.server.service;

import com.natura.web.server.model.User;
import com.natura.web.server.provider.UserProvider;

import java.util.Optional;

public class UserService {

    private final UserProvider userProvider;

    public UserService(UserProvider userProvider) {
        this.userProvider = userProvider;
    }

    public Optional<User> getUserById(Long userId) {
        return userProvider.getUserById(userId);
    }

    public User save(User user) {
        return userProvider.save(user);
    }

    public Optional<User> getUserByEmail(String email) {
        return userProvider.getUserByEmail(email);
    }

    public Optional<User> getUserByUsername(String username) {
        return userProvider.getUserByUsername(username);
    }
}
