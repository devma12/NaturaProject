package com.natura.web.server.repo;

import com.natura.web.server.entities.Insect;
import org.springframework.data.repository.CrudRepository;

public interface InsectRepository extends CrudRepository<Insect, Long> {
}
