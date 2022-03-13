package com.natura.web.server.services;

import com.natura.web.server.exceptions.DataNotFoundException;
import com.natura.web.server.exceptions.InvalidDataException.InvalidFileException;
import com.natura.web.server.model.Entry;
import com.natura.web.server.model.Identification;
import com.natura.web.server.model.Image;
import com.natura.web.server.model.Species;
import com.natura.web.server.model.User;
import com.natura.web.server.providers.EntryProvider;
import com.natura.web.server.providers.IdentificationProvider;
import com.natura.web.server.providers.SpeciesProvider;
import com.natura.web.server.providers.UserProvider;

import java.io.InputStream;
import java.util.Date;
import java.util.Optional;

public class EntryService {

    private final UserProvider userProvider;

    private final EntryProvider entryProvider;

    private final SpeciesProvider speciesProvider;

    private final IdentificationProvider identificationProvider;

    private final ImageService imageService;

    public EntryService(final ImageService imageService,
                        final UserProvider userProvider,
                        final EntryProvider entryProvider,
                        final SpeciesProvider speciesProvider,
                        final IdentificationProvider identificationProvider) {
        this.imageService = imageService;
        this.userProvider = userProvider;
        this.entryProvider = entryProvider;
        this.speciesProvider = speciesProvider;
        this.identificationProvider = identificationProvider;
    }

    public Entry createEntryAndSuggestedIdentification(Entry entry, String filename, String contentType, InputStream inputStream, Long userId, Long speciesId)
            throws DataNotFoundException, InvalidFileException {

        // Check user exists
        Optional<User> createdBy = userProvider.getUserById(userId);
        if (createdBy.isEmpty()) {
            throw new DataNotFoundException(User.class, "id", userId);
        }
        entry.setCreatedBy(createdBy.get());

        // Create image and store it in db
        Image image = imageService.upload(filename, contentType, inputStream);
        entry.setImage(image);

        // Save new entry
        Entry saved = entryProvider.save(entry);

        // Create identification if any
        Optional<Species> species = speciesProvider.getSpeciesById(speciesId);
        if (species.isPresent()) {
            Identification creatorProposal = new Identification(saved, species.get(), createdBy.get(), new Date());
            identificationProvider.save(creatorProposal);
        }
        return saved;
    }
}
