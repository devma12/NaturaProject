package com.natura.web.server.controllers;

import com.natura.web.server.entities.Insect;
import com.natura.web.server.entities.User;
import com.natura.web.server.exceptions.ServerException;
import com.natura.web.server.repo.InsectRepository;
import com.natura.web.server.repo.UserRepository;
import com.natura.web.server.services.EntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(path="/natura-api/insect")
public class InsectController {

    @Autowired
    private EntryService entryService;

    @Autowired
    private InsectRepository insectRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping(path="/new")
    public @ResponseBody  Insect create(@RequestParam("imageFile") MultipartFile file,
                         @RequestParam("name") String name,
                         @RequestParam("date") Date date,
                         @RequestParam("description") String description,
                         @RequestParam("location") String location,
                         @RequestParam("species") String species,
                         @RequestParam("createdBy") String createdBy) throws IOException {

        Long userId = Long.parseLong(createdBy);
        Long speciesId = Long.parseLong(species);
        Insect insect = new Insect(name, date, description, location);
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

    @GetMapping(path="/creator/{id}")
    @ResponseBody
    public List<Insect> getByCreator(@PathVariable String id) {
        Long userId = Long.parseLong(id);
        User createdBy = userRepository.findById(userId).orElse(null);
        if (createdBy != null) {
            return this.insectRepository.findByCreatedBy(createdBy);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
        }
    }
}
