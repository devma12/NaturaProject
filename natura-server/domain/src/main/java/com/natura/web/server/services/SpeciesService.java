package com.natura.web.server.services;

import com.natura.web.server.exceptions.InvalidDataException;
import com.natura.web.server.model.Phenology;
import com.natura.web.server.model.Species;
import com.natura.web.server.providers.SpeciesProvider;

import java.time.Month;
import java.util.List;
import java.util.Optional;

public class SpeciesService {

    private final SpeciesProvider speciesProvider;

    public SpeciesService(final SpeciesProvider speciesProvider) {
        this.speciesProvider = speciesProvider;
    }

    public Species create(Species species) throws InvalidDataException {

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


        if (species.getPhenologies() != null && species.getPhenologies().size() > 0) {
            // Check phenology start is before phenology end
            for (Phenology phenology : species.getPhenologies()) {
                if (phenology.getStart().compareTo(phenology.getEnd()) > 0) {
                    throw new InvalidDataException("Invalid phenology: Start and end months should be reversed.");
                }
            }

            if (species.getPhenologies().size() > 1) {
                // Check phenologies do not overlap each other
                if (phenologiesOverlap(species.getPhenologies()))
                    throw new InvalidDataException("Invalid phenologies: Periods overlap.");
            }
        }


        Species created = speciesProvider.save(species);
        return created;
    }

    private boolean phenologiesOverlap(List<Phenology> phenologies) {
        for (int i = 0; i < phenologies.size(); i++) {
            for (int j = 0; j < phenologies.size(); j++) {
                if (phenologies.get(i) != null && phenologies.get(j) != null
                        && phenologies.get(i) != phenologies.get(j)) {
                    Month iStart = phenologies.get(i).getStart();
                    Month iEnd = phenologies.get(i).getEnd();
                    Month jStart = phenologies.get(j).getStart();
                    Month jEnd = phenologies.get(j).getEnd();

                    if (iStart.compareTo(jStart) < 0) {
                        if (iEnd.compareTo(jStart) >= 0) {
                            return true;
                        }
                    } else {
                        if (jEnd.compareTo(iStart) >= 0) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
