package com.natura.web.server.repo;

import com.natura.web.server.entities.Comment;
import com.natura.web.server.entities.Identification;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Long> {

    List<Comment> findByIdentification(Identification identification);
}
