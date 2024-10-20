package com.natura.web.server.repository;

import com.natura.web.server.entities.Species;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpeciesRepository extends CrudRepository<Species, Long> {

  public List<Species> findByType(Species.Type type);

  public Species findByCommonName(String commonName);

  public Species findByScientificName(String scientificName);
}
