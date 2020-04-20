package com.natura.web.server.services;

import com.natura.web.server.entities.Entry;
import com.natura.web.server.entities.User;
import com.natura.web.server.exceptions.DataNotFoundException;
import com.natura.web.server.repo.EntryRepository;
import com.natura.web.server.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EntryService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntryRepository entryRepository;

    public Entry create(Entry entry, Long userId) throws DataNotFoundException {

        User createdBy = userRepository.findById(userId).orElse(null);
        if (createdBy != null) {
            entry.setCreatedBy(createdBy);
            return entryRepository.save(entry);
        } else {
            throw new DataNotFoundException(User.class, "id", userId);
        }
    }
}
