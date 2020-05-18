package com.natura.web.server.services;

import com.natura.web.server.entities.Entry;
import com.natura.web.server.entities.Identification;
import com.natura.web.server.entities.Species;
import com.natura.web.server.entities.User;
import com.natura.web.server.exceptions.DataNotFoundException;
import com.natura.web.server.repo.EntryRepository;
import com.natura.web.server.repo.IdentificationRepository;
import com.natura.web.server.repo.SpeciesRepository;
import com.natura.web.server.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class IdentificationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntryRepository entryRepository;

    @Autowired
    private SpeciesRepository speciesRepository;

    @Autowired
    private IdentificationRepository identificationRepository;

    public Identification identify(Long entryId, Long speciesId, Long userId) throws DataNotFoundException {

        // Get proposer user
        User suggestedBy = userRepository.findById(userId).orElse(null);
        if (suggestedBy  == null) {
            throw new DataNotFoundException(User.class, "id", userId);
        }

        // Get entry
        Entry entry = entryRepository.findById(entryId).orElse(null);
        if (entry  == null) {
            throw new DataNotFoundException(Entry.class, "id", entryId);
        }

        // Get species
        Species species = speciesRepository.findById(speciesId).orElse(null);
        if (species  == null) {
            throw new DataNotFoundException(Species.class, "id", speciesId);
        }

        Identification identification = new Identification(entry, species, suggestedBy, new Date());
        return identificationRepository.save(identification);

    }
}
