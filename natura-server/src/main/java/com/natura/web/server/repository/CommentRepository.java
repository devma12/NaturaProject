package com.natura.web.server.repository;

import com.natura.web.server.entities.Comment;
import com.natura.web.server.entities.Identification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {

    List<Comment> findByIdentification(Identification identification);
}
