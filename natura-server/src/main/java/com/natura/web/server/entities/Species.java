package com.natura.web.server.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;

import java.util.List;
import java.util.Set;

@Entity
public class Species extends ValidableItem implements Serializable {

    public static enum Type {
        Flower,
        Insect;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name="common_name", unique=true, nullable = false)
    private String commonName;

    @Column(name="scientific_name", unique=true, nullable = false)
    private String scientificName;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(name="classification_order")
    private String order;

    @Column(name="family")
    private String family;


    @OneToMany(mappedBy = "species", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Phenology> phenologies;

    private String habitatType;

    @ManyToMany
    @JoinTable(
            name = "species_criteria",
            joinColumns = @JoinColumn(name = "species_id"),
            inverseJoinColumns = @JoinColumn(name = "criteria_id"))
    private Set<Criteria> criteria;

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

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public List<Phenology> getPhenologies() {
        return phenologies;
    }

    public void setPhenologies(List<Phenology> phenologies) {
        this.phenologies = phenologies;
    }

    public String getHabitatType() {
        return habitatType;
    }

    public void setHabitatType(String habitatType) {
        this.habitatType = habitatType;
    }

}
