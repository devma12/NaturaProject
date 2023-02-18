package com.natura.web.server.provider;

import com.natura.web.server.model.Species;
import com.natura.web.server.model.SpeciesType;

import java.util.List;
import java.util.Optional;

public interface SpeciesProvider {
    Optional<Species> getSpeciesById(Long speciesId);

    Optional<Species> getSpeciesByScientificName(String scientificName);

    Optional<Species> getSpeciesByCommonName(String commonName);

    Species save(Species species);

    List<Species> getSpeciesByType(SpeciesType type);

    List<Species> getSpecies();

}
