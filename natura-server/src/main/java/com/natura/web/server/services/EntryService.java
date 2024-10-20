package com.natura.web.server.services;

import com.natura.web.server.entities.Entry;
import com.natura.web.server.entities.Identification;
import com.natura.web.server.entities.Image;
import com.natura.web.server.entities.Species;
import com.natura.web.server.entities.User;
import com.natura.web.server.exceptions.DataNotFoundException;
import com.natura.web.server.repository.EntryRepository;
import com.natura.web.server.repository.IdentificationRepository;
import com.natura.web.server.repository.SpeciesRepository;
import com.natura.web.server.repository.UserRepository;
import java.io.IOException;
import java.util.Date;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Service
public class EntryService {

  private UserRepository userRepository;

  private EntryRepository entryRepository;

  private SpeciesRepository speciesRepository;

  private IdentificationRepository identificationRepository;

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
        identificationRepository.save(creatorProposal);
      }

      return saved;
    } else {
      throw new DataNotFoundException(User.class, "id", userId);
    }
  }
}
