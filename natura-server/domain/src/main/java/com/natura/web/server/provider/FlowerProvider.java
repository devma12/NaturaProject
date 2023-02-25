package com.natura.web.server.provider;

import com.natura.web.server.model.Flower;

import java.util.List;
import java.util.Optional;

public interface FlowerProvider {

    Optional<Flower> getFlowerById(Long id);

    List<Flower> getFlowers();

    List<Flower> getFlowersByCreatedBy(Long userId);

}
