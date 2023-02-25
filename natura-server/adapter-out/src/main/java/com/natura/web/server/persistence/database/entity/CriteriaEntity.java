package com.natura.web.server.persistence.database.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import java.io.Serializable;
import java.util.Set;

@Entity(name = "criteria")
public class CriteriaEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    private String value;

    @ManyToMany(mappedBy = "criteria")
    @JsonIgnore
    private Set<SpeciesEntity> species;

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

    public Set<SpeciesEntity> getSpecies() {
        return species;
    }

    public void setSpecies(Set<SpeciesEntity> species) {
        this.species = species;
    }
}
