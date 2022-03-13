package com.natura.web.server.ports.database.repo;

import com.natura.web.server.ports.database.entities.FlowerEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FlowerRepository extends CrudRepository<FlowerEntity, Long> {

    public List<FlowerEntity> findByCreatedById(Long userId);
}
