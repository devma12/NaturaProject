package com.natura.web.server.persistence.database;

import com.natura.web.server.mapper.SpeciesMapper;
import com.natura.web.server.model.Species;
import com.natura.web.server.model.SpeciesType;
import com.natura.web.server.persistence.database.entity.SpeciesEntity;
import com.natura.web.server.persistence.database.repository.SpeciesRepository;
import com.natura.web.server.provider.SpeciesProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
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

    @Override
    public List<Species> getSpeciesByType(SpeciesType type) {
        return mapper.map(speciesRepository.findByType(type));
    }

    @Override
    public List<Species> getSpecies() {
        return mapper.map((List<SpeciesEntity>) speciesRepository.findAll());
    }
}
