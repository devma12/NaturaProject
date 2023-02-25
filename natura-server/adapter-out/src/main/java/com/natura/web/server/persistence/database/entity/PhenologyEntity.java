package com.natura.web.server.persistence.database.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.Month;

@Entity(name = "Phenology")
public class PhenologyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Month start;

    private Month end;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "species_id", referencedColumnName = "id")
    @JsonBackReference
    private SpeciesEntity species;

    public PhenologyEntity() {
    }

    public PhenologyEntity(Month start, Month end) {
        this.start = start;
        this.end = end;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Month getStart() {
        return start;
    }

    public void setStart(Month start) {
        this.start = start;
    }

    public Month getEnd() {
        return end;
    }

    public void setEnd(Month end) {
        this.end = end;
    }

    public SpeciesEntity getSpecies() {
        return species;
    }

    public void setSpecies(SpeciesEntity species) {
        this.species = species;
    }

}