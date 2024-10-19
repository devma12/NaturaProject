package com.natura.web.server.repository;

import com.natura.web.server.entities.Flower;
import com.natura.web.server.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlowerRepository extends CrudRepository<Flower, Long> {

    public List<Flower> findByCreatedBy(User user);
}
