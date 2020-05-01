package com.natura.web.server.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Species extends ValidableItem {

    public static enum Type {
        FLOWER,
        INSECT;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="common_name", unique=true, nullable = false)
    private String commonName;

    @Column(name="scientific_name", unique=true, nullable = false)
    private String scientificName;

    @Enumerated(EnumType.STRING)
    private Type type;

    @JsonManagedReference
    @ManyToMany
    @JoinTable(
            name = "species_criteria",
            joinColumns = @JoinColumn(name = "species_id"),
            inverseJoinColumns = @JoinColumn(name = "criteria_id"))
    Set<Criteria> criteria;

    public Species() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public Set<Criteria> getCriteria() {
        return criteria;
    }

    public void setCriteria(Set<Criteria> criteria) {
        this.criteria = criteria;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

}
