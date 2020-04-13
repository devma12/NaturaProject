package com.natura.web.server.services;

import com.natura.web.server.entities.User;
import com.natura.web.server.exceptions.InvalidDataException;
import com.natura.web.server.exceptions.MandatoryDataAccountException;
import com.natura.web.server.exceptions.ServerException;
import com.natura.web.server.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User register(String email, String username, String password) throws ServerException {

        if (email == null)
            throw new MandatoryDataAccountException("Email");
        if (username == null)
            throw new MandatoryDataAccountException("Username");
        if (password == null)
            throw new MandatoryDataAccountException("Password");

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
