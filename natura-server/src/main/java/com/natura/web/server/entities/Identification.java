package com.natura.web.server.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Identification implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "entry_id")
    private Entry entry;

    @Id
    @ManyToOne
    @JoinColumn(name = "species_id")
    private Species species;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User suggestedBy;

    private Date suggestedDate;

    @ManyToOne
    @JoinColumn(name = "validator_id")
    private User validatedBy;

    private Date validatedDate;

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    public Species getSpecies() {
        return species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    public User getSuggestedBy() {
        return suggestedBy;
    }

    public void setSuggestedBy(User suggestedBy) {
        this.suggestedBy = suggestedBy;
    }

    public Date getSuggestedDate() {
        return suggestedDate;
    }

    public void setSuggestedDate(Date suggestedDate) {
        this.suggestedDate = suggestedDate;
    }

    public User getValidatedBy() {
        return validatedBy;
    }

    public void setValidatedBy(User validatedBy) {
        this.validatedBy = validatedBy;
    }

    public Date getValidatedDate() {
        return validatedDate;
    }

    public void setValidatedDate(Date validatedDate) {
        this.validatedDate = validatedDate;
    }

}
