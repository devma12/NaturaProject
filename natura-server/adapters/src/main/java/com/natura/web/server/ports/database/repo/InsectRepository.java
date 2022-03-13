package com.natura.web.server.ports.database.repo;

import com.natura.web.server.ports.database.entities.InsectEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface InsectRepository extends CrudRepository<InsectEntity, Long> {

    public List<InsectEntity> findByCreatedById(Long userId);
}
