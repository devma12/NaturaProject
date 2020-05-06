package com.natura.web.server.controllers;

import com.natura.web.server.entities.Flower;
import com.natura.web.server.entities.Insect;
import com.natura.web.server.exceptions.ServerException;
import com.natura.web.server.repo.InsectRepository;
import com.natura.web.server.services.EntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(path="/natura-api/insect")
public class InsectController {

    @Autowired
    private EntryService entryService;

    @Autowired
    private InsectRepository insectRepository;

    @PostMapping(path="/new")
    public @ResponseBody  Insect create(@RequestParam("imageFile") MultipartFile file,
                         @RequestParam("name") String name,
                         @RequestParam("description") String description,
                         @RequestParam("location") String location,
                         @RequestParam("species") String species,
                         @RequestParam("createdBy") String createdBy) throws IOException {

        Long userId = Long.parseLong(createdBy);
        Long speciesId = Long.parseLong(species);
        Insect insect = new Insect(name, description, location);
        try {
            return (Insect) entryService.create(insect, file, userId, speciesId);
        } catch (ServerException e) {
            throw e.responseStatus();
        }
    }

    @GetMapping(path="/all")
    public @ResponseBody
    List<Insect> getAllInsects() {

        return (List<Insect>) this.insectRepository.findAll();
    }

    @GetMapping(path="/{id}")
    @ResponseBody
    public Insect getById(@PathVariable String id) {
        Long entryId = Long.parseLong(id);
        return this.insectRepository.findById(entryId).orElse(null);
    }
}
