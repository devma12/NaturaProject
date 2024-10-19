package com.natura.web.server.controllers;

import com.natura.web.server.entities.Species;
import com.natura.web.server.exceptions.ServerException;
import com.natura.web.server.repository.SpeciesRepository;
import com.natura.web.server.services.SpeciesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/natura-api/species")
public class SpeciesController {

    @Autowired
    private SpeciesService speciesService;

    @Autowired
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
