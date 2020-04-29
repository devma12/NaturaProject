package com.natura.web.server.repo;

import com.natura.web.server.entities.Flower;
import org.springframework.data.repository.CrudRepository;

public interface FlowerRepository extends CrudRepository<Flower, Long>  {
}
