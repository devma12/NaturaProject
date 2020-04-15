package com.natura.web.server.services;

import com.natura.web.server.entities.User;
import com.natura.web.server.exceptions.InvalidDataException;
import com.natura.web.server.exceptions.UserAccountException;
import com.natura.web.server.exceptions.ServerException;
import com.natura.web.server.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User register(String email, String username, String password) throws ServerException {

        if (email == null)
            throw new UserAccountException.MandatoryUserDetailException("Email");
        if (username == null)
            throw new UserAccountException.MandatoryUserDetailException("Username");
        if (password == null)
            throw new UserAccountException.MandatoryUserDetailException("Password");

        // Verify there is no existing account with same email address
        User found = userRepository.findByEmail(email);
        if (found != null) {
            throw new UserAccountException.DuplicateAccountException("email: " + email);
        }

        // Verify there is no existing account with same username
        found = userRepository.findByUsername(username);
        if (found != null) {
            throw new UserAccountException.DuplicateAccountException("username: " + username);
        }

        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        user = userRepository.save(user);
        return user;
    }

    public User login(String email, String password) throws ServerException {

        User user = userRepository.findByEmail(email);
        if (user == null)
            throw new InvalidDataException("email address");

        if (password != null && password.equals(user.getPassword())) {
            return user;
        } else {
            throw new InvalidDataException("password");
        }
    }

}
