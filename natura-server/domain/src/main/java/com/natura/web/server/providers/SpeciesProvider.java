package com.natura.web.server.providers;

import com.natura.web.server.model.Species;

import java.util.Optional;

public interface SpeciesProvider {
    Optional<Species> getSpeciesById(Long speciesId);

    Optional<Species> getSpeciesByScientificName(String scientificName);

    Optional<Species> getSpeciesByCommonName(String commonName);

    Species save(Species species);
}
