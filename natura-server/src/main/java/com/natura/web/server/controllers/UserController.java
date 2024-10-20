package com.natura.web.server.controllers;

import com.natura.web.server.entities.User;
import com.natura.web.server.exceptions.ServerException;
import com.natura.web.server.repository.UserRepository;
import com.natura.web.server.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/natura-api/user")
public class UserController {

  private UserService userService;

  private UserRepository userRepository;

  @PostMapping(path = "/register")
  public User register(@RequestParam String email, @RequestParam String username, @RequestParam String password) {

    try {
      return userService.register(email, username, password);
    } catch (ServerException e) {
      throw e.responseStatus();
    }
  }

  @PostMapping(path = "/login")
  public User login(@RequestParam String username, @RequestParam String password) {

    try {
      return userService.login(username, password);
    } catch (ServerException e) {
      throw e.responseStatus();
    }
  }

  @GetMapping(path = "/authenticate")
  public User authenticate() {

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
        if (user != null) {
            return ResponseEntity.ok().build();
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred when trying to log out.");
        }
    } catch (ServerException e) {
      throw e.responseStatus();
    }
  }

  @GetMapping(path = "/all")
  public Iterable<User> getAllUsers() {
    return userRepository.findAll();
  }

  @GetMapping(path = "/{id}")
  public User getUser(@PathVariable Long id) {

    return userRepository.findById(id).orElse(null);
  }

  @PutMapping(path = "/email/{id}")
  public User changeUserEmail(@PathVariable Long id, @RequestBody String email) {
    try {
      return userService.updateEmail(id, email);
    } catch (ServerException e) {
      throw e.responseStatus();
    }
  }

  @PutMapping(path = "/password/{id}")
  public User changeUserPassword(@PathVariable Long id, @RequestParam("old") String oldPwd, @RequestParam("new") String newPwd) {
    try {
      return userService.updatePassword(id, oldPwd, newPwd);
    } catch (ServerException e) {
      throw e.responseStatus();
    }
  }
}
