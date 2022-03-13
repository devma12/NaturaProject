package com.natura.web.server.ports.database.repo;

import com.natura.web.server.ports.database.entities.CommentEntity;
import com.natura.web.server.ports.database.entities.IdentificationEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepository extends CrudRepository<CommentEntity, Long> {

    List<CommentEntity> findByIdentification(IdentificationEntity identification);
}
