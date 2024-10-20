package com.natura.web.server.controllers;

import com.natura.web.server.entities.Flower;
import com.natura.web.server.entities.User;
import com.natura.web.server.exceptions.ServerException;
import com.natura.web.server.repository.FlowerRepository;
import com.natura.web.server.repository.UserRepository;
import com.natura.web.server.services.EntryService;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/natura-api/flower")
public class FlowerController {

  private EntryService entryService;

  private FlowerRepository flowerRepository;

  private UserRepository userRepository;

  @PostMapping(path = "/new")
  public Flower create(@RequestParam("imageFile") MultipartFile file,
      @RequestParam("name") String name,
      @RequestParam("date") Date date,
      @RequestParam("description") String description,
      @RequestParam("location") String location,
      @RequestParam("species") String species,
      @RequestParam("createdBy") String createdBy) throws IOException {

    Long userId = Long.parseLong(createdBy);
    Long speciesId = Long.parseLong(species);
    Flower flower = new Flower(name, date, description, location);
    try {
      return (Flower) entryService.create(flower, file, userId, speciesId);
    } catch (ServerException e) {
      throw e.responseStatus();
    }
  }

  @GetMapping(path = "/all")
  public List<Flower> getAllFlowers() {

    return (List<Flower>) this.flowerRepository.findAll();
  }

  @GetMapping(path = "/{id}")
  public Flower getById(@PathVariable String id) {
    Long entryId = Long.parseLong(id);
    return this.flowerRepository.findById(entryId).orElse(null);
  }

  @GetMapping(path = "/creator/{id}")
  public List<Flower> getByCreator(@PathVariable String id) {
    Long userId = Long.parseLong(id);
    User createdBy = userRepository.findById(userId).orElse(null);
    if (createdBy != null) {
      return this.flowerRepository.findByCreatedBy(createdBy);
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
    }
  }

}
