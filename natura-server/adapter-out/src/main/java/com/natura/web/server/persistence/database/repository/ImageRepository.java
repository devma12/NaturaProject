package com.natura.web.server.persistence.database.repository;

import com.natura.web.server.persistence.database.entity.ImageEntity;
import org.springframework.data.repository.CrudRepository;

public interface ImageRepository extends CrudRepository<ImageEntity, Long> {
}
