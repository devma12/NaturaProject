package com.natura.web.server.persistence.database;

import com.natura.web.server.mapper.UserMapper;
import com.natura.web.server.model.User;
import com.natura.web.server.persistence.database.repository.UserRepository;
import com.natura.web.server.provider.UserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DatabaseUserProvider implements UserProvider {

    @Autowired
    UserMapper mapper;

    @Autowired
    UserRepository userRepository;

    @Override
    public Optional<User> getUserById(Long userId) {
        return mapper.map(userRepository.findById(userId));
    }

    @Override
    public User save(User user) {
        return mapper.map(userRepository.save(mapper.map(user)));
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return mapper.map(userRepository.findByEmail(email));
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return mapper.map(userRepository.findByUsername(username));
    }
}
