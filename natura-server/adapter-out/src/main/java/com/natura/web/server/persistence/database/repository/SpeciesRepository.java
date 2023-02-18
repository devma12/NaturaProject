package com.natura.web.server.persistence.database.repository;

import com.natura.web.server.model.SpeciesType;
import org.springframework.data.repository.CrudRepository;
import com.natura.web.server.persistence.database.entity.SpeciesEntity;

import java.util.List;
import java.util.Optional;

public interface SpeciesRepository extends CrudRepository<SpeciesEntity, Long> {

    List<SpeciesEntity> findByType(SpeciesType type);

    Optional<SpeciesEntity> findByCommonName(String commonName);

    Optional<SpeciesEntity> findByScientificName(String scientificName);
}
