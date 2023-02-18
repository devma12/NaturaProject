package com.natura.web.server.controller;

import com.natura.web.server.exception.InvalidDataException;
import com.natura.web.server.model.Species;
import com.natura.web.server.model.SpeciesType;
import com.natura.web.server.service.SpeciesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(path = "/natura-api/species")
public class SpeciesController {

    @Autowired
    private SpeciesService speciesService;

    @GetMapping(path = "/all")
    @ResponseBody
    public List<Species> getAllSpecies() {
        return speciesService.getSpecies();
    }

    @GetMapping(path = "/type/{type}")
    @ResponseBody
    public List<Species> getByType(@PathVariable("type") SpeciesType type) {
        return speciesService.getSpeciesByType(type);
    }

    @PostMapping(path = "/new")
    @ResponseBody
    public Species create(@RequestBody Species species) throws InvalidDataException {
        return speciesService.create(species);
    }

}
