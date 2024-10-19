package com.natura.web.server.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.*;

@Entity
public class Identification implements Serializable {

    @EmbeddedId
    @GeneratedValue(strategy = GenerationType.AUTO)
    private IdentificationKey id;

    //    @MapsId("id")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "entry_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private Entry entry;

    //    @MapsId("id")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "species_id", nullable = false, insertable = false, updatable = false)
    private Species species;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User suggestedBy;

    private Date suggestedDate;

    @ManyToOne
    @JoinColumn(name = "validator_id")
    private User validatedBy;

    private Date validatedDate;

    @OneToMany(mappedBy = "identification")
    @JsonManagedReference
    private List<Comment> comments;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "identification_like",
        joinColumns = {
            @JoinColumn(name = "entry_id", referencedColumnName = "entry_id"),
            @JoinColumn(name = "species_id", referencedColumnName = "species_id"),
        },
        inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> likes;

    public Identification() {

        this.comments = new ArrayList<>();
        this.likes = new HashSet<>();
    }

    public Identification(Entry entry, Species species, User proposer, Date date) {
        this();
        Assert.notNull(entry, "Entry cannot be null to define an identification.");
        Assert.notNull(species, "Species cannot be null to define an identification.");

        this.id = new IdentificationKey(entry.getId(), species.getId());
        this.entry = entry;
        this.species = species;
        this.suggestedBy = proposer;
        this.suggestedDate = date;

    }

    public IdentificationKey getId() {
        return id;
    }

    public void setId(IdentificationKey id) {
        this.id = id;
    }

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

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Set<User> getLikes() {
        return likes;
    }

    public void setLikes(Set<User> likes) {
        this.likes = likes;
    }

}
