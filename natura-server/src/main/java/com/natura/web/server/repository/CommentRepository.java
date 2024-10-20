package com.natura.web.server.repository;

import com.natura.web.server.entities.Comment;
import com.natura.web.server.entities.Identification;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {

  List<Comment> findByIdentification(Identification identification);
}
