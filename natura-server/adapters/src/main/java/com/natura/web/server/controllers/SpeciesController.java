package com.natura.web.server.controllers;

import com.natura.web.server.exceptions.InvalidDataException;
import com.natura.web.server.ports.database.entities.SpeciesEntity;
import com.natura.web.server.model.Species;
import com.natura.web.server.ports.database.repo.SpeciesRepository;
import com.natura.web.server.services.SpeciesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(path = "/natura-api/species")
public class SpeciesController {

    @Autowired
    private SpeciesService speciesService;

    @Autowired
    private SpeciesRepository speciesRepository;

    @GetMapping(path = "/all")
    @ResponseBody
    public List<SpeciesEntity> getAllSpecies() throws IOException {
        List<SpeciesEntity> species = (List<SpeciesEntity>) speciesRepository.findAll();
        return species;
    }

    @GetMapping(path = "/type/{type}")
    @ResponseBody
    public List<SpeciesEntity> getByType(@PathVariable("type") SpeciesEntity.Type type) throws IOException {
        List<SpeciesEntity> species = speciesRepository.findByType(type);
        return species;
    }

    @PostMapping(path = "/new")
    @ResponseBody
    public Species create(@RequestBody Species species) throws InvalidDataException {
        return speciesService.create(species);
    }

}
