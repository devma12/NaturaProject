package com.natura.web.server.repository;

import com.natura.web.server.entities.Insect;
import com.natura.web.server.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InsectRepository extends CrudRepository<Insect, Long> {

    public List<Insect> findByCreatedBy(User user);
}
