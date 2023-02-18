package com.natura.web.server.persistence.database.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import java.util.Set;

@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, columnDefinition = "VARCHAR(500)")
    private String token;

    @Column(name = "flower_validator")
    private boolean flowerValidator;

    @Column(name = "insect_validator")
    private boolean insectValidator;

    @ManyToMany(mappedBy = "likes")
    @JsonIgnore
    private Set<IdentificationEntity> liked;

    public UserEntity() {
        this.flowerValidator = false;
        this.insectValidator = false;
    }

    public UserEntity(String username) {
        this();
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isFlowerValidator() {
        return flowerValidator;
    }

    public void setFlowerValidator(boolean flowerValidator) {
        this.flowerValidator = flowerValidator;
    }

    public boolean isInsectValidator() {
        return insectValidator;
    }

    public void setInsectValidator(boolean insectValidator) {
        this.insectValidator = insectValidator;
    }
}
