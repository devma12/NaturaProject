package com.natura.web.server.services;

import com.natura.web.server.entities.*;
import com.natura.web.server.exceptions.DataNotFoundException;
import com.natura.web.server.repo.EntryRepository;
import com.natura.web.server.repo.IdentificationRepository;
import com.natura.web.server.repo.SpeciesRepository;
import com.natura.web.server.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

@Service
public class EntryService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntryRepository entryRepository;

    @Autowired
    private SpeciesRepository speciesRepository;

    @Autowired
    private IdentificationRepository identificationRepository;

    @Autowired
    private ImageService imageService;

    public Entry create(Entry entry, MultipartFile file, Long userId, Long speciesId) throws DataNotFoundException, IOException {

        // Check user exists
        User createdBy = userRepository.findById(userId).orElse(null);
        if (createdBy != null) {
            entry.setCreatedBy(createdBy);

            // Create image and store it in db
            Image image = imageService.upload(file);
            entry.setImage(image);

            // Save new entry
            Entry saved = entryRepository.save(entry);

            // Create identification if any
            Species species = speciesRepository.findById(speciesId).orElse(null);
            if (species != null) {
                Identification creatorProposal = new Identification(saved, species, createdBy, new Date());
                Identification savedIdentification = identificationRepository.save(creatorProposal);
            }

            return saved;
        } else {
            throw new DataNotFoundException(User.class, "id", userId);
        }
    }
}
