package com.natura.web.server.provider;

import com.natura.web.server.model.Entry;

import java.util.Optional;

public interface EntryProvider {
    Entry save(Entry entry);

    Optional<Entry> getEntryById(Long entryId);
}
