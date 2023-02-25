package com.natura.web.server.persistence.database.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "identification")
public class IdentificationEntity implements Serializable {

    @EmbeddedId
    @GeneratedValue(strategy = GenerationType.AUTO)
    private IdentificationKey id;

    @MapsId("entry_id")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "entry_id", nullable = false, insertable = false, updatable = false)
    private EntryEntity entry;

    @MapsId("species_id")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "species_id", nullable = false, insertable = false, updatable = false)
    private SpeciesEntity species;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity suggestedBy;

    private Date suggestedDate;

    @ManyToOne
    @JoinColumn(name = "validator_id")
    private UserEntity validatedBy;

    private Date validatedDate;

    @OneToMany(mappedBy = "identification")
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonManagedReference
    private List<CommentEntity> comments;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "identification_like",
            joinColumns = {
                    @JoinColumn(name = "entry_id", referencedColumnName = "entry_id"),
                    @JoinColumn(name = "species_id", referencedColumnName = "species_id"),
            },
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<UserEntity> likes;

    public IdentificationEntity() {

        this.comments = new ArrayList<CommentEntity>();
        this.likes = new HashSet<UserEntity>();
    }

    public IdentificationEntity(EntryEntity entry, SpeciesEntity species, UserEntity proposer, Date date) {
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

    public EntryEntity getEntry() {
        return entry;
    }

    public void setEntry(EntryEntity entry) {
        this.entry = entry;
    }

    public SpeciesEntity getSpecies() {
        return species;
    }

    public void setSpecies(SpeciesEntity species) {
        this.species = species;
    }

    public UserEntity getSuggestedBy() {
        return suggestedBy;
    }

    public void setSuggestedBy(UserEntity suggestedBy) {
        this.suggestedBy = suggestedBy;
    }

    public Date getSuggestedDate() {
        return suggestedDate;
    }

    public void setSuggestedDate(Date suggestedDate) {
        this.suggestedDate = suggestedDate;
    }

    public UserEntity getValidatedBy() {
        return validatedBy;
    }

    public void setValidatedBy(UserEntity validatedBy) {
        this.validatedBy = validatedBy;
    }

    public Date getValidatedDate() {
        return validatedDate;
    }

    public void setValidatedDate(Date validatedDate) {
        this.validatedDate = validatedDate;
    }

    public List<CommentEntity> getComments() {
        return comments;
    }

    public void setComments(List<CommentEntity> comments) {
        this.comments = comments;
    }

    public Set<UserEntity> getLikes() {
        return likes;
    }

    public void setLikes(Set<UserEntity> likes) {
        this.likes = likes;
    }

}
