package com.natura.web.server.service;

import com.natura.web.server.model.Flower;
import com.natura.web.server.provider.FlowerProvider;

import java.util.List;
import java.util.Optional;

public class FlowerService {

    private final FlowerProvider flowerProvider;

    public FlowerService(final FlowerProvider flowerProvider) {
        this.flowerProvider = flowerProvider;
    }

    public Optional<Flower> getFlowerById(Long entryId) {
        return flowerProvider.getFlowerById(entryId);
    }

    public List<Flower> getFlowers() {
        return flowerProvider.getFlowers();
    }

    public List<Flower> getFlowersByCreator(Long userId) {
        return flowerProvider.getFlowersByCreatedBy(userId);
    }
}
