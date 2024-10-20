package com.natura.web.server.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
@Entity
public class Species extends ValidableItem implements Serializable {

  public enum Type {
    FLOWER,
    INSECT;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "common_name", unique = true, nullable = false)
  private String commonName;

  @Column(name = "scientific_name", unique = true, nullable = false)
  private String scientificName;

  @Enumerated(EnumType.STRING)
  private Type type;

  @Column(name = "classification_order")
  private String order;

  @Column(name = "family")
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

}
