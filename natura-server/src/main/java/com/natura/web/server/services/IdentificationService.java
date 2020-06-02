package com.natura.web.server.services;

import com.natura.web.server.entities.*;
import com.natura.web.server.exceptions.DataNotFoundException;
import com.natura.web.server.exceptions.InvalidDataException;
import com.natura.web.server.exceptions.UserAccountException;
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

        // To Do: check no identification already exists for entry + species couple
        // To Do: check species and entry types are consistent

        Identification identification = new Identification(entry, species, suggestedBy, new Date());
        return identificationRepository.save(identification);

    }

    public Identification validate(Long entryId, Long speciesId, Long userId)
            throws DataNotFoundException, InvalidDataException, UserAccountException.ValidationPermissionException {

        // Get validator user
        User validator = userRepository.findById(userId).orElse(null);
        if (validator  == null) {
            throw new DataNotFoundException(User.class, "id", userId);
        }

        // Get identification
        Identification identification = identificationRepository.findByIdEntryIdAndIdSpeciesId(entryId, speciesId);
        if (identification  == null) {
            throw new DataNotFoundException(Identification.class, "id", "{ entry : "  + entryId + ", species: " + speciesId + " }");
        }

        // Check user has the right to validate this identification
        if (identification.getEntry() != null && identification.getEntry() instanceof Flower) {
            if (!validator.isFlowerValidator())
                throw new UserAccountException.ValidationPermissionException(validator.getUsername(), Species.Type.Flower);
        } else if (identification.getEntry() != null && identification.getEntry() instanceof Insect) {
            if (!validator.isInsectValidator())
                throw new UserAccountException.ValidationPermissionException(validator.getUsername(), Species.Type.Insect);
        } else {
            throw new InvalidDataException("entry of identification { entry : "  + entryId + ", species: " + speciesId + " }");
        }
        if (identification.getSuggestedBy() != null && validator.getUsername() == identification.getSuggestedBy().getUsername()) {
            throw new UserAccountException.ValidationPermissionException("User has not the permission to validate its own suggested identification.");
        }

        // validate identification and save
        identification.setValidatedBy(validator);
        identification.setValidatedDate(new Date());
        return identificationRepository.save(identification);

    }
}
