package com.natura.web.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class User {

    private Long id;

    private String email;

    private String username;

    private String password;

    private String token;

    private boolean flowerValidator;

    private boolean insectValidator;

    private Set<Identification> liked;

    public User() {
        this.flowerValidator = false;
        this.insectValidator = false;
    }

    public User(String username) {
        this();
        this.username = username;
    }
}
