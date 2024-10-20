package com.natura.web.server.controllers;

import com.natura.web.server.entities.Comment;
import com.natura.web.server.entities.Identification;
import com.natura.web.server.exceptions.ServerException;
import com.natura.web.server.repository.IdentificationRepository;
import com.natura.web.server.services.IdentificationService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/natura-api/identification")
public class IdentificationController {

  private IdentificationRepository identificationRepository;

  private IdentificationService identificationService;

  @GetMapping(path = "/entry/{id}")
  public List<Identification> getByEntry(@PathVariable("id") String id) {

    Long entryId = Long.parseLong(id);
    return identificationRepository.findByIdEntryId(entryId);
  }

  @PostMapping(path = "/new")
  public Identification identify(@RequestParam("entryId") String entry,
      @RequestParam("speciesId") String species,
      @RequestParam("userId") String proposer) {

    Long entryId = Long.parseLong(entry);
    Long speciesId = Long.parseLong(species);
    Long userId = Long.parseLong(proposer);
    try {
      return identificationService.identify(entryId, speciesId, userId);
    } catch (ServerException e) {
      throw e.responseStatus();
    }
  }

  @PostMapping(path = "/validate")
  public Identification validate(@RequestParam("entry") String entry,
      @RequestParam("species") String species,
      @RequestParam("validator") String validator) {

    Long entryId = Long.parseLong(entry);
    Long speciesId = Long.parseLong(species);
    Long userId = Long.parseLong(validator);
    try {
      // validate identification
      Identification identification = identificationService.validate(entryId, speciesId, userId);

      // Check if proposer should become a validator
      identificationService.giveValidatorRights(identification.getSuggestedBy());

      return identification;
    } catch (ServerException e) {
      throw e.responseStatus();
    }
  }

  @PostMapping(path = "/comment")
  public Comment comment(@RequestParam("entry") String entry,
      @RequestParam("species") String species,
      @RequestParam("observer") String observer,
      @RequestParam("comment") String text) {

    Long entryId = Long.parseLong(entry);
    Long speciesId = Long.parseLong(species);
    Long userId = Long.parseLong(observer);
    try {
      // comment identification
      return identificationService.comment(entryId, speciesId, userId, text);
    } catch (ServerException e) {
      throw e.responseStatus();
    }
  }

  @GetMapping(path = "/comments")
  public List<Comment> getComments(@RequestParam("entry") String entry,
      @RequestParam("species") String species) {

    Long entryId = Long.parseLong(entry);
    Long speciesId = Long.parseLong(species);
    try {
      return identificationService.getIdentificationComments(entryId, speciesId);
    } catch (ServerException e) {
      throw e.responseStatus();
    }
  }

  @PutMapping(path = "/like")
  public Identification like(@RequestParam("entry") String entry,
      @RequestParam("species") String species,
      @RequestParam("user") String user) {
    Long entryId = Long.parseLong(entry);
    Long speciesId = Long.parseLong(species);
    Long userId = Long.parseLong(user);
    try {
      return identificationService.like(entryId, speciesId, userId);
    } catch (ServerException e) {
      throw e.responseStatus();
    }
  }
}
