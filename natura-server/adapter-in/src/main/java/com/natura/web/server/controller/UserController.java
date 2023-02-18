package com.natura.web.server.controller;

import com.natura.web.server.exception.DataNotFoundException;
import com.natura.web.server.exception.ServerException;
import com.natura.web.server.exception.UserAccountException;
import com.natura.web.server.model.User;
import com.natura.web.server.service.AuthenticationService;
import com.natura.web.server.service.UserService;
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

    // @Lazy
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserService userService;

    @PostMapping(path = "/register")
    public @ResponseBody
    User register(@RequestParam String email, @RequestParam String username, @RequestParam String password) throws ServerException {
        return authenticationService.register(email, username, password);
    }

    @PostMapping(path = "/login")
    public @ResponseBody
    User login(@RequestParam String username, @RequestParam String password) throws ServerException {
        return authenticationService.login(username, password);
    }

    @GetMapping(path = "/authenticate")
    public @ResponseBody
    User authenticate() throws UserAccountException.AuthenticationException, UserAccountException.InvalidAccountDataException {
        return authenticationService.authenticate();
    }

    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletRequest request, HttpServletResponse response) throws UserAccountException.AuthenticationException, UserAccountException.InvalidAccountDataException {
        User user = authenticationService.logout(request, response);
        if (user != null)
            return ResponseEntity.ok().build();
        else
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred when trying to log out.");
    }

    @GetMapping(path = "/{id}")
    public @ResponseBody
    User getUser(@PathVariable Long id) {
        // FIXME : return user and not entity
        return userService.getUserById(id).orElse(null);
    }

    @PutMapping(path = "/email/{id}")
    public @ResponseBody
    User changeUserEmail(@PathVariable Long id, @RequestBody String email) throws DataNotFoundException, UserAccountException.DuplicateAccountException {
        return authenticationService.updateEmail(id, email);
    }

    @PutMapping(path = "/password/{id}")
    public @ResponseBody
    User changeUserPassword(@PathVariable Long id,
                            @RequestParam("old") String oldPwd,
                            @RequestParam("new") String newPwd) throws DataNotFoundException, UserAccountException.AuthenticationException {
        return authenticationService.updatePassword(id, oldPwd, newPwd);
    }
}
