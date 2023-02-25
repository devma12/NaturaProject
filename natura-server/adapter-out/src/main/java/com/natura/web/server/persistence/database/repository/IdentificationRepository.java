package com.natura.web.server.persistence.database.repository;

import com.natura.web.server.model.IdentificationKey;
import com.natura.web.server.persistence.database.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import com.natura.web.server.persistence.database.entity.IdentificationEntity;

import java.util.List;
import java.util.Optional;

public interface IdentificationRepository extends CrudRepository<IdentificationEntity, IdentificationKey> {

    List<IdentificationEntity> findByIdEntryId(Long entryId);

    List<IdentificationEntity> findByIdSpeciesId(Long species);

    Optional<IdentificationEntity> findByIdEntryIdAndIdSpeciesId(Long entry, Long species);

    List<IdentificationEntity> findBySuggestedBy(UserEntity user);
}
