package com.natura.web.server.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.time.Month;

@Entity
public class Phenology {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private Month start;

    private Month end;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "species_id", referencedColumnName = "id")
    @JsonBackReference
    private Species species;

    public Phenology() { }

    public Phenology(Month start, Month end) {
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

    public Species getSpecies() {
        return species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

}
