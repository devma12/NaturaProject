package com.natura.web.server.persistence.database.repository;

import org.springframework.data.repository.CrudRepository;
import com.natura.web.server.persistence.database.entity.FlowerEntity;

import java.util.List;

public interface FlowerRepository extends CrudRepository<FlowerEntity, Long> {

    public List<FlowerEntity> findByCreatedById(Long userId);
}
