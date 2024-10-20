package com.natura.web.server.repository;

import com.natura.web.server.entities.Identification;
import com.natura.web.server.entities.IdentificationKey;
import com.natura.web.server.entities.User;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdentificationRepository extends CrudRepository<Identification, IdentificationKey> {

  List<Identification> findByIdEntryId(Long entryId);

  List<Identification> findByIdSpeciesId(Long species);

  Identification findByIdEntryIdAndIdSpeciesId(Long entry, Long species);

  List<Identification> findBySuggestedBy(User user);
}
