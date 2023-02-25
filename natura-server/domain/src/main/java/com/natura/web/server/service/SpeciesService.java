package com.natura.web.server.service;

import com.natura.web.server.exception.InvalidDataException;
import com.natura.web.server.model.Phenology;
import com.natura.web.server.model.Species;
import com.natura.web.server.model.SpeciesType;
import com.natura.web.server.provider.SpeciesProvider;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Optional;

public class SpeciesService {

    private final SpeciesProvider speciesProvider;

    public SpeciesService(final SpeciesProvider speciesProvider) {
        this.speciesProvider = speciesProvider;
    }

    public List<Species> getSpeciesByType(SpeciesType type) {
        return speciesProvider.getSpeciesByType(type);
    }

    public List<Species> getSpecies() {
        return speciesProvider.getSpecies();
    }

    public Species create(final Species species) throws InvalidDataException {

        // Check species with same name does not already exist in db
        String commonName = species.getCommonName().toLowerCase();
        String scientificName = species.getScientificName().toLowerCase();

        Optional<Species> duplicate = speciesProvider.getSpeciesByScientificName(scientificName);
        if (duplicate.isPresent()) {
            throw new InvalidDataException.DuplicateDataException("Species", "scientific name", scientificName);
        }

        duplicate = speciesProvider.getSpeciesByCommonName(commonName);
        if (duplicate.isPresent()) {
            throw new InvalidDataException.DuplicateDataException("Species", "common name", commonName);
        }

        if (CollectionUtils.isNotEmpty(species.getPhenologies())) {
            // Check phenology start is before phenology end
            for (Phenology phenology : species.getPhenologies()) {
                if (phenology.getStart().compareTo(phenology.getEnd()) > 0) {
                    throw new InvalidDataException("Invalid phenology: Start and end months should be reversed.");
                }
            }
            // Check phenologies do not overlap each other
            if (species.getPhenologies().size() > 1 && phenologiesOverlap(species.getPhenologies())) {
                throw new InvalidDataException("Invalid phenologies: Periods overlap.");
            }
        }
        return speciesProvider.save(species);
    }

    private boolean phenologiesOverlap(List<Phenology> phenologies) {
        for (int i = 0; i < phenologies.size(); i++) {
            for (int j = 0; j < phenologies.size(); j++) {
                if (phenologies.get(i) != null && phenologies.get(j) != null
                        && phenologies.get(i) != phenologies.get(j)
                        && phenologies.get(i).getRange().overlap(phenologies.get(j).getRange())) {
                    return true;
                }
            }
        }
        return false;
    }
}
