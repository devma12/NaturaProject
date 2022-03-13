package com.natura.web.server.ports.database.repo;

import com.natura.web.server.ports.database.entities.EntryEntity;
import org.springframework.data.repository.CrudRepository;

public interface EntryRepository extends CrudRepository<EntryEntity, Long> {
}
