package com.natura.web.server.persistence.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class IdentificationKey implements Serializable {

    @Column(name = "entry_id")
    private Long entryId;

    @Column(name = "species_id")
    private Long speciesId;

    public IdentificationKey() {
    }

    public IdentificationKey(Long entry, Long species) {
        this.entryId = entry;
        this.speciesId = species;
    }

    public Long getEntryId() {
        return entryId;
    }

    public void setEntryId(Long entryId) {
        this.entryId = entryId;
    }

    public Long getSpeciesId() {
        return speciesId;
    }

    public void setSpeciesId(Long speciesId) {
        this.speciesId = speciesId;
    }

}