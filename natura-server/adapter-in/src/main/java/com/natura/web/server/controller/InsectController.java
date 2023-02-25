package com.natura.web.server.controller;

import com.natura.web.server.exception.DataNotFoundException;
import com.natura.web.server.exception.InvalidDataException;
import com.natura.web.server.model.Insect;
import com.natura.web.server.service.EntryService;
import com.natura.web.server.service.InsectService;
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

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "/natura-api/insect")
public class InsectController {

    @Autowired
    private EntryService entryService;

    @Autowired
    private InsectService insectService;

    @PostMapping(path = "/new")
    public @ResponseBody
    Insect create(@RequestParam("imageFile") MultipartFile file,
                  @RequestParam("name") String name,
                  @RequestParam("date") Date date,
                  @RequestParam("description") String description,
                  @RequestParam("location") String location,
                  @RequestParam("species") String species,
                  @RequestParam("createdBy") String createdBy) throws IOException, DataNotFoundException, InvalidDataException.InvalidFileException {

        Long userId = Long.parseLong(createdBy);
        Long speciesId = Long.parseLong(species);
        Insect insect = new Insect(name, date, description, location);
        return (Insect) entryService.createEntryAndSuggestedIdentification(insect, file.getOriginalFilename(), file.getContentType(), file.getInputStream(), userId, speciesId);
    }

    @GetMapping(path = "/all")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<Insect> getAllInsects() {
        return this.insectService.getInsects();
    }

    @GetMapping(path = "/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Insect getById(@PathVariable String id) throws DataNotFoundException {
        Long entryId = Long.parseLong(id);
        Optional<Insect> found = this.insectService.getInsectById(entryId);
        if (found.isPresent()) {
            return found.get();
        } else {
            throw new DataNotFoundException(Insect.class, "id", entryId);
        }
    }

    @GetMapping(path = "/creator/{id}")
    @ResponseBody
    public List<Insect> getByCreator(@PathVariable String id) {
        Long userId = Long.parseLong(id);
        return this.insectService.getInsectsByCreator(userId);
    }
}
