package com.natura.web.server.controllers;

import com.natura.web.server.exceptions.DataNotFoundException;
import com.natura.web.server.exceptions.InvalidDataException.InvalidFileException;
import com.natura.web.server.model.Flower;
import com.natura.web.server.services.EntryService;
import com.natura.web.server.services.FlowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "/natura-api/flower")
public class FlowerController {

    @Autowired
    private EntryService entryService;

    @Autowired
    private FlowerService flowerService;

    @PostMapping(path = "/new")
    @ResponseStatus(HttpStatus.CREATED)
    // FIXME: uri should be only natura-api/flower without /new ? maybe not, there is no body ??
    public @ResponseBody
    Flower create(@RequestParam("imageFile") MultipartFile file,
                  @RequestParam("name") String name,
                  @RequestParam("date") Date date,
                  @RequestParam("description") String description,
                  @RequestParam("location") String location,
                  @RequestParam("species") String species,
                  @RequestParam("createdBy") String createdBy) throws DataNotFoundException, InvalidFileException, IOException {
        Long userId = parseId(createdBy, "creator id");
        Long speciesId = parseId(species, "species id");
        Flower flower = new Flower(name, date, description, location);
        return (Flower) entryService.createEntryAndSuggestedIdentification(flower, file.getOriginalFilename(), file.getContentType(), file.getInputStream(), userId, speciesId);
    }

    @GetMapping(path = "/all") // FIXME: uri should be only natura-api/flower without /all
    @ResponseBody
    public List<Flower> getAllFlowers() {
        return this.flowerService.getFlowers();
    }

    @GetMapping(path = "/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Flower getById(@PathVariable String id) throws DataNotFoundException {
        Long entryId = parseId(id, "flower id");
        Optional<Flower> found = this.flowerService.getFlowerById(entryId);
        if (found.isPresent()) {
            return found.get();
        } else {
            throw new DataNotFoundException(Flower.class, "id", entryId);
        }
    }

    @GetMapping(path = "/creator/{id}")
    @ResponseBody
    public List<Flower> getByCreator(@PathVariable String id) {
        Long userId = parseId(id, "creator id");
        return this.flowerService.getFlowersByCreator(userId);
    }

    private static Long parseId(String id, String name) {
        try {
            return Long.parseLong(id);
        } catch (NumberFormatException e) {
            String message = String.format("Number format is invalid for %s : %s.", name, id);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, message, e);
        }
    }

}
