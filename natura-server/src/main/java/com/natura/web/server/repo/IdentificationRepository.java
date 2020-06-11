package com.natura.web.server.repo;

import com.natura.web.server.entities.Identification;
import com.natura.web.server.entities.IdentificationKey;
import com.natura.web.server.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IdentificationRepository extends CrudRepository<Identification, IdentificationKey> {

    List<Identification> findByIdEntryId(Long entryId);

    List<Identification> findByIdSpeciesId(Long species);

    Identification findByIdEntryIdAndIdSpeciesId(Long entry, Long species);

    List<Identification> findBySuggestedBy(User user);
}
