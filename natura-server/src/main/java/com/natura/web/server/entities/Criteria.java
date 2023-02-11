package com.natura.web.server.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity(name = "criteria")
public class Criteria implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    private String value;

    @ManyToMany(mappedBy = "criteria")
    @JsonIgnore
    private Set<Species> species;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Set<Species> getSpecies() {
        return species;
    }

    public void setSpecies(Set<Species> species) {
        this.species = species;
    }
}
