package com.natura.web.server.services;

import com.natura.web.server.entities.Phenology;
import com.natura.web.server.entities.Species;
import com.natura.web.server.exceptions.InvalidDataException;
import com.natura.web.server.repo.SpeciesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.List;

@Service
public class SpeciesService {

    @Autowired
    private SpeciesRepository speciesRepository;

    public Species create(Species species) throws InvalidDataException {

        // Check species with same name does not already exist in db
        String commonName = species.getCommonName().toLowerCase();
        String scientificName = species.getScientificName().toLowerCase();

        Species duplicate = speciesRepository.findByScientificName(scientificName);
        if (duplicate != null) {
            throw new InvalidDataException.DuplicateDataException("Species", "scientific name", scientificName);
        }

        duplicate = speciesRepository.findByCommonName(commonName);
        if (duplicate != null) {
            throw new InvalidDataException.DuplicateDataException("Species", "common name", commonName);
        }


        if (species.getPhenologies() != null && species.getPhenologies().size() > 0) {
            // Check phenology start is before phenology end
            for (Phenology phenology: species.getPhenologies()) {
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



        Species created = speciesRepository.save(species);
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
