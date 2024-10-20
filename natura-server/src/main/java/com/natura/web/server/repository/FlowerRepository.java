package com.natura.web.server.repository;

import com.natura.web.server.entities.Flower;
import com.natura.web.server.entities.User;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlowerRepository extends CrudRepository<Flower, Long> {

  public List<Flower> findByCreatedBy(User user);
}
