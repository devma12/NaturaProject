package com.natura.web.server.persistence.database;

import com.natura.web.server.model.Flower;
import com.natura.web.server.provider.FlowerProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.natura.web.server.mapper.EntryMapper;
import com.natura.web.server.persistence.database.repository.FlowerRepository;

import java.util.List;
import java.util.Optional;

@Component
public class DatabaseFlowerProvider implements FlowerProvider {

    @Autowired
    EntryMapper mapper;

    @Autowired
    FlowerRepository flowerRepository;

    @Override
    public Optional<Flower> getFlowerById(Long id) {
        return mapper.mapFlowerOptional(flowerRepository.findById(id));
    }

    @Override
    public List<Flower> getFlowers() {
        return mapper.mapFlowerList(flowerRepository.findAll());
    }

    @Override
    public List<Flower> getFlowersByCreatedBy(Long userId) {
        return mapper.mapFlowerList(flowerRepository.findByCreatedById(userId));
    }
}
