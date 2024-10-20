package com.natura.web.server.controllers;

import com.natura.web.server.entities.Species;
import com.natura.web.server.exceptions.ServerException;
import com.natura.web.server.repository.SpeciesRepository;
import com.natura.web.server.services.SpeciesService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/natura-api/species")
public class SpeciesController {

  private SpeciesService speciesService;

  private SpeciesRepository speciesRepository;

  @GetMapping(path = "/all")
  public List<Species> getAllSpecies() {
    return (List<Species>) speciesRepository.findAll();
  }

  @GetMapping(path = "/type/{type}")
  public List<Species> getByType(@PathVariable("type") Species.Type type) {
    return speciesRepository.findByType(type);
  }

  @PostMapping(path = "/new")
  public Species create(@RequestBody Species species) {
    try {
      return speciesService.create(species);
    } catch (ServerException e) {
      throw e.responseStatus();
    }
  }

}
