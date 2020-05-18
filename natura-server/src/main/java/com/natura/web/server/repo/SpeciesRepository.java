package com.natura.web.server.repo;

import com.natura.web.server.entities.Species;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SpeciesRepository extends CrudRepository<Species, Long> {

    public List<Species> findByType(Species.Type type);

    public Species findByCommonName(String commonName);

    public Species findByScientificName(String scientificName);
}
