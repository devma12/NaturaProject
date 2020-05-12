package com.natura.web.server.controllers;

import com.natura.web.server.entities.Species;
import com.natura.web.server.repo.SpeciesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(path="/natura-api/species")
public class SpeciesController {

    @Autowired
    private SpeciesRepository speciesRepository;

    @GetMapping(path="/all")
    @ResponseBody
    public List<Species> getAllSpecies() throws IOException {
        List<Species> species = (List<Species>) speciesRepository.findAll();
        return species;
    }

    @GetMapping(path="/type/{type}")
    @ResponseBody
    public List<Species> getByType(@PathVariable("type") Species.Type type) throws IOException {
        List<Species> species = speciesRepository.findByType(type);
        return species;
    }

    @PostMapping(path="/new")
    @ResponseBody
    public Species create(@RequestBody Species species) {
        Species created = speciesRepository.save(species);
        return created;
    }

}
