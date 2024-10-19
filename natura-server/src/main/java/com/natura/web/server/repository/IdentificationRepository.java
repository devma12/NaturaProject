package com.natura.web.server.repository;

import com.natura.web.server.entities.Identification;
import com.natura.web.server.entities.IdentificationKey;
import com.natura.web.server.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IdentificationRepository extends CrudRepository<Identification, IdentificationKey> {

    List<Identification> findByIdEntryId(Long entryId);

    List<Identification> findByIdSpeciesId(Long species);

    Identification findByIdEntryIdAndIdSpeciesId(Long entry, Long species);

    List<Identification> findBySuggestedBy(User user);
}
