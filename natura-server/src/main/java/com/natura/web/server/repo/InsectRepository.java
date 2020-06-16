package com.natura.web.server.repo;

import com.natura.web.server.entities.Insect;
import com.natura.web.server.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface InsectRepository extends CrudRepository<Insect, Long> {

    public List<Insect> findByCreatedBy(User user);
}
