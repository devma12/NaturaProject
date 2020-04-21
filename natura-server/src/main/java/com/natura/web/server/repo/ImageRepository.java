package com.natura.web.server.repo;

import com.natura.web.server.entities.Image;
import org.springframework.data.repository.CrudRepository;

public interface ImageRepository extends CrudRepository<Image, Long> {
}
