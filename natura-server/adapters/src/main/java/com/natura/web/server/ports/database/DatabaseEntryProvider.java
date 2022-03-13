package com.natura.web.server.ports.database;

import com.natura.web.server.mappers.EntryMapper;
import com.natura.web.server.model.Entry;
import com.natura.web.server.providers.EntryProvider;
import com.natura.web.server.ports.database.repo.EntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DatabaseEntryProvider implements EntryProvider {

    @Autowired
    EntryMapper mapper;

    @Autowired
    EntryRepository entryRepository;

    @Override
    public Entry save(Entry entry) {
        return mapper.mapEntryEntity(entryRepository.save(mapper.mapEntry(entry)));
    }

    @Override
    public Optional<Entry> getEntryById(Long entryId) {
        return mapper.map(entryRepository.findById(entryId));
    }
}
