package com.natura.web.server.persistence.database;

import com.natura.web.server.mapper.EntryMapper;
import com.natura.web.server.model.Insect;
import com.natura.web.server.persistence.database.repository.InsectRepository;
import com.natura.web.server.provider.InsectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class DatabaseInsectProvider implements InsectProvider {

    @Autowired
    EntryMapper mapper;

    @Autowired
    InsectRepository insectRepository;

    @Override
    public Optional<Insect> getInsectById(Long id) {
        return mapper.mapInsectOptional(insectRepository.findById(id));
    }

    @Override
    public List<Insect> getInsects() {
        return mapper.mapInsectList(insectRepository.findAll());
    }

    @Override
    public List<Insect> getInsectsByCreatedBy(Long userId) {
        return mapper.mapInsectList(insectRepository.findByCreatedById(userId));
    }
}
