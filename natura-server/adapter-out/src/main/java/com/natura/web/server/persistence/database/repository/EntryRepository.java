package com.natura.web.server.persistence.database.repository;

import org.springframework.data.repository.CrudRepository;
import com.natura.web.server.persistence.database.entity.EntryEntity;

public interface EntryRepository extends CrudRepository<EntryEntity, Long> {
}
