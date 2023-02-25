package com.natura.web.server.model;

import lombok.Data;

import java.util.*;

@Data
public class Identification {

    private IdentificationKey id;

    private Entry entry;

    private Species species;

    private User suggestedBy;

    private Date suggestedDate;

    private User validatedBy;

    private Date validatedDate;

    private List<Comment> comments;

    private Set<User> likes;

    public Identification() {
        this.comments = new ArrayList<>();
        this.likes = new HashSet<>();
    }

    public Identification(Entry entry, Species species, User proposer, Date date) {
        this();
        if (entry == null) {
            throw new RuntimeException("Entry cannot be null to define an identification.");
        }
        if (species == null) {
            throw new RuntimeException("Species cannot be null to define an identification.");
        }
        this.id = new IdentificationKey(entry.getId(), species.getId());
        this.entry = entry;
        this.species = species;
        this.suggestedBy = proposer;
        this.suggestedDate = date;
    }
}
