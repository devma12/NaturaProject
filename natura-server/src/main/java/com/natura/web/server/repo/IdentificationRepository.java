package com.natura.web.server.repo;

import com.natura.web.server.entities.Identification;
import com.natura.web.server.entities.IdentificationKey;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IdentificationRepository extends CrudRepository<Identification, IdentificationKey> {

    List<Identification> findByIdEntryId(Long entryId);

    List<Identification> findByIdSpeciesId(Long species);
}
