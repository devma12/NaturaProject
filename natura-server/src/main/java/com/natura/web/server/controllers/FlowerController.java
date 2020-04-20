package com.natura.web.server.controllers;

import com.natura.web.server.entities.Flower;
import com.natura.web.server.entities.User;
import com.natura.web.server.exceptions.ServerException;
import com.natura.web.server.repo.EntryRepository;
import com.natura.web.server.services.EntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path="/natura-api/flower")
public class FlowerController {

    @Autowired
    private EntryService entryService;

    @PostMapping(path="/new")
    public @ResponseBody
    Flower create(@RequestBody Flower flower, @RequestParam String createdBy) {
        Long userId = Long.parseLong(createdBy);
        try {
            return (Flower) entryService.create(flower, userId);
        } catch (ServerException e) {
            throw e.responseStatus();
        }
    }
}
