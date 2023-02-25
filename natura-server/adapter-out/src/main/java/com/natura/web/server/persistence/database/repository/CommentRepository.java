package com.natura.web.server.persistence.database.repository;

import com.natura.web.server.persistence.database.entity.CommentEntity;
import org.springframework.data.repository.CrudRepository;
import com.natura.web.server.persistence.database.entity.IdentificationEntity;

import java.util.List;

public interface CommentRepository extends CrudRepository<CommentEntity, Long> {

    List<CommentEntity> findByIdentification(IdentificationEntity identification);
}
