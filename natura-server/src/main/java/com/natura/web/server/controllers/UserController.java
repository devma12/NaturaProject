package com.natura.web.server.controllers;

import com.natura.web.server.entities.User;
import com.natura.web.server.exceptions.ServerException;
import com.natura.web.server.repo.UserRepository;
import com.natura.web.server.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path="/natura-api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping(path="/register")
    public @ResponseBody User register(@RequestParam String email, @RequestParam String username, @RequestParam String password) {

        try {
            return userService.register(email, username, password);
        } catch (ServerException e) {
            throw e.responseStatus();
        }
    }

    @PostMapping(path="/login")
    public @ResponseBody User login(@RequestParam String email, @RequestParam String password) {

        try {
            return userService.login(email, password);
        } catch (ServerException e) {
            throw e.responseStatus();
        }
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<User> getAllUsers() {

        return userRepository.findAll();
    }

    @GetMapping(path="/{id}")
    public @ResponseBody User getUser(@PathVariable Integer id) {

        return userRepository.findById(id).orElse(null);
    }
}
