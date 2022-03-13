package com.natura.web.server.controllers;

import com.natura.web.server.exceptions.DataNotFoundException;
import com.natura.web.server.exceptions.ServerException;
import com.natura.web.server.exceptions.UserAccountException;
import com.natura.web.server.ports.database.entities.UserEntity;
import com.natura.web.server.model.User;
import com.natura.web.server.ports.database.repo.UserRepository;
import com.natura.web.server.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(path = "/natura-api/user")
public class UserController {

    @Lazy
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping(path = "/register")
    public @ResponseBody
    User register(@RequestParam String email, @RequestParam String username, @RequestParam String password) throws ServerException {
        return userService.register(email, username, password);
    }

    @PostMapping(path = "/login")
    public @ResponseBody
    User login(@RequestParam String username, @RequestParam String password) throws ServerException {
        return userService.login(username, password);
    }

    @GetMapping(path = "/authenticate")
    public @ResponseBody
    User authenticate() throws UserAccountException.AuthenticationException, UserAccountException.InvalidAccountDataException {
        return userService.authenticate();
    }

    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletRequest request, HttpServletResponse response) throws UserAccountException.AuthenticationException, UserAccountException.InvalidAccountDataException {
        User user = userService.logout(request, response);
        if (user != null)
            return ResponseEntity.ok().build();
        else
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred when trying to log out.");
    }

    @GetMapping(path = "/all")
    public @ResponseBody
    Iterable<UserEntity> getAllUsers() {

        return userRepository.findAll();
    }

    @GetMapping(path = "/{id}")
    public @ResponseBody
    UserEntity getUser(@PathVariable Long id) {
        // FIXME : return user and not entity
        return userRepository.findById(id).orElse(null);
    }

    @PutMapping(path = "/email/{id}")
    public @ResponseBody
    User changeUserEmail(@PathVariable Long id, @RequestBody String email) throws DataNotFoundException, UserAccountException.DuplicateAccountException {
        return userService.updateEmail(id, email);
    }

    @PutMapping(path = "/password/{id}")
    public @ResponseBody
    User changeUserPassword(@PathVariable Long id,
                            @RequestParam("old") String oldPwd,
                            @RequestParam("new") String newPwd) throws DataNotFoundException, UserAccountException.AuthenticationException {
        return userService.updatePassword(id, oldPwd, newPwd);
    }
}
