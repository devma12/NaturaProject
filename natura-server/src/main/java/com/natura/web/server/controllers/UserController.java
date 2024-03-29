package com.natura.web.server.controllers;

import com.natura.web.server.entities.User;
import com.natura.web.server.exceptions.ServerException;
import com.natura.web.server.repo.UserRepository;
import com.natura.web.server.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(path="/natura-api/user")
public class UserController {

    @Lazy
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
    public @ResponseBody User login(@RequestParam String username, @RequestParam String password) {

        try {
            return userService.login(username, password);
        } catch (ServerException e) {
            throw e.responseStatus();
        }
    }

    @GetMapping(path="/authenticate")
    public @ResponseBody User authenticate() {

        try {
            return userService.authenticate();
        } catch (ServerException e) {
            throw e.responseStatus();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletRequest request, HttpServletResponse response) {

        try {
            User user = userService.logout(request, response);
            if (user != null)
                return ResponseEntity.ok().build();
            else
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred when trying to log out.");
        } catch (ServerException e) {
            throw e.responseStatus();
        }
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<User> getAllUsers() {

        return userRepository.findAll();
    }

    @GetMapping(path="/{id}")
    public @ResponseBody User getUser(@PathVariable Long id) {

        return userRepository.findById(id).orElse(null);
    }

    @PutMapping(path = "/email/{id}")
    public @ResponseBody User changeUserEmail(@PathVariable Long id, @RequestBody String email) {
        try {
            return userService.updateEmail(id, email);
        } catch (ServerException e) {
            throw e.responseStatus();
        }
    }

    @PutMapping(path = "/password/{id}")
    public @ResponseBody User changeUserPassword(@PathVariable Long id,
                            @RequestParam("old") String oldPwd,
                            @RequestParam("new") String newPwd) {
        try {
            return userService.updatePassword(id, oldPwd, newPwd);
        } catch (ServerException e) {
            throw e.responseStatus();
        }
    }
}
