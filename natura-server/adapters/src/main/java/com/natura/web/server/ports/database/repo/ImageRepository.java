package com.natura.web.server.ports.database.repo;

import com.natura.web.server.ports.database.entities.ImageEntity;
import org.springframework.data.repository.CrudRepository;

public interface ImageRepository extends CrudRepository<ImageEntity, Long> {
}
