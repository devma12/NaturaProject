package com.natura.web.server.persistence.database;

import com.natura.web.server.model.Entry;
import com.natura.web.server.persistence.database.repository.EntryRepository;
import com.natura.web.server.provider.EntryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.natura.web.server.mapper.EntryMapper;

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
