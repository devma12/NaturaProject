package com.natura.web.server.repo;

import com.natura.web.server.entities.Flower;
import com.natura.web.server.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FlowerRepository extends CrudRepository<Flower, Long>  {

    public List<Flower> findByCreatedBy(User user);
}
