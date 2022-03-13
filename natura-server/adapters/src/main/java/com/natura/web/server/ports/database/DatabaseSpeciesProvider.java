package com.natura.web.server.ports.database;

import com.natura.web.server.mappers.SpeciesMapper;
import com.natura.web.server.model.Species;
import com.natura.web.server.providers.SpeciesProvider;
import com.natura.web.server.ports.database.repo.SpeciesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DatabaseSpeciesProvider implements SpeciesProvider {

    @Autowired
    SpeciesMapper mapper;

    @Autowired
    SpeciesRepository speciesRepository;

    @Override
    public Optional<Species> getSpeciesById(Long speciesId) {
        return mapper.map(speciesRepository.findById(speciesId));
    }

    @Override
    public Optional<Species> getSpeciesByScientificName(String scientificName) {
        return mapper.map(speciesRepository.findByScientificName(scientificName));
    }

    @Override
    public Optional<Species> getSpeciesByCommonName(String commonName) {
        return mapper.map(speciesRepository.findByCommonName(commonName));
    }

    @Override
    public Species save(Species species) {
        return mapper.map(speciesRepository.save(mapper.map(species)));
    }
}
