package com.natura.web.server.persistence.database.repository;

import org.springframework.data.repository.CrudRepository;
import com.natura.web.server.persistence.database.entity.InsectEntity;

import java.util.List;

public interface InsectRepository extends CrudRepository<InsectEntity, Long> {

    public List<InsectEntity> findByCreatedById(Long userId);
}
