package com.natura.web.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class Species extends ValidableItem {

    private Long id;

    private String commonName;

    private String scientificName;

    private SpeciesType type;

    private String order;

    private String family;

    private List<Phenology> phenologies;

    private String habitatType;

    private Set<Criteria> criteria;

    public Species() {
        super();
    }

}
