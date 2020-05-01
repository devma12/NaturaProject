package com.natura.web.server.repo;

import com.natura.web.server.entities.Entry;
import org.springframework.data.repository.CrudRepository;

public interface EntryRepository extends CrudRepository<Entry, Long> {
}
