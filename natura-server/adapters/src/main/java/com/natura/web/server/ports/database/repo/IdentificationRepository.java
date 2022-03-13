package com.natura.web.server.ports.database.repo;

import com.natura.web.server.ports.database.entities.IdentificationEntity;
import com.natura.web.server.ports.database.entities.IdentificationKey;
import com.natura.web.server.ports.database.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface IdentificationRepository extends CrudRepository<IdentificationEntity, IdentificationKey> {

    List<IdentificationEntity> findByIdEntryId(Long entryId);

    List<IdentificationEntity> findByIdSpeciesId(Long species);

    Optional<IdentificationEntity> findByIdEntryIdAndIdSpeciesId(Long entry, Long species);

    List<IdentificationEntity> findBySuggestedBy(UserEntity user);
}
