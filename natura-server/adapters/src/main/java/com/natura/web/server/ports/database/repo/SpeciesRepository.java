package com.natura.web.server.ports.database.repo;

import com.natura.web.server.ports.database.entities.SpeciesEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface SpeciesRepository extends CrudRepository<SpeciesEntity, Long> {

    public List<SpeciesEntity> findByType(SpeciesEntity.Type type);

    public Optional<SpeciesEntity> findByCommonName(String commonName);

    public Optional<SpeciesEntity> findByScientificName(String scientificName);
}
