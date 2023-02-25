package com.natura.web.server.persistence.database.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.natura.web.server.model.SpeciesType;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity(name = "species")
public class SpeciesEntity extends ValidableItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "common_name", unique = true, nullable = false)
    private String commonName;

    @Column(name = "scientific_name", unique = true, nullable = false)
    private String scientificName;

    @Enumerated(EnumType.STRING)
    private SpeciesType type;

    @Column(name = "classification_order")
    private String order;

    @Column(name = "family")
    private String family;

    @OneToMany(mappedBy = "species", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<PhenologyEntity> phenologies;

    private String habitatType;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "species_criteria",
            joinColumns = @JoinColumn(name = "species_id"),
            inverseJoinColumns = @JoinColumn(name = "criteria_id"))
    private Set<CriteriaEntity> criteria;

    public SpeciesEntity() {
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

    public Set<CriteriaEntity> getCriteria() {
        return criteria;
    }

    public void setCriteria(Set<CriteriaEntity> criteria) {
        this.criteria = criteria;
    }

    public SpeciesType getType() {
        return type;
    }

    public void setType(SpeciesType type) {
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

    public List<PhenologyEntity> getPhenologies() {
        return phenologies;
    }

    public void setPhenologies(List<PhenologyEntity> phenologies) {
        this.phenologies = phenologies;
    }

    public String getHabitatType() {
        return habitatType;
    }

    public void setHabitatType(String habitatType) {
        this.habitatType = habitatType;
    }

}
