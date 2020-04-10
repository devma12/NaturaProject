package com.natura.web.server.controllers;

import com.natura.web.server.entities.User;
import com.natura.web.server.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path="/natura/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping(path="/create")
    public @ResponseBody User addNewUser (@RequestParam String email, @RequestParam String password) {

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user = userRepository.save(user);
        return user;
    }

    @PostMapping(path="/login")
    public @ResponseBody User login(@RequestParam String email, @RequestParam String password) throws Exception {

        User user = userRepository.findByEmail(email);
        if (user == null)
            throw new Exception("Invalid email address");

        boolean validPwd = password == user.getPassword();
        if (validPwd) {
            return user;
        } else {
            throw new Exception("Invalid password");
        }

    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<User> getAllUsers() {

        return userRepository.findAll();
    }
}
