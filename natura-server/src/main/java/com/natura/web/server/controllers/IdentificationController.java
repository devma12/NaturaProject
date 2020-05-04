package com.natura.web.server.controllers;

import com.natura.web.server.entities.Identification;
import com.natura.web.server.entities.Species;
import com.natura.web.server.repo.EntryRepository;
import com.natura.web.server.repo.IdentificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(path="/natura-api/identification")
public class IdentificationController {

    @Autowired
    IdentificationRepository identificationRepository;

    @GetMapping(path="/entry/{id}")
    public @ResponseBody
    List<Identification> getByEntry(@PathVariable("id") String id) throws IOException {

        Long entryId = Long.parseLong(id);
        List<Identification> identifications = identificationRepository.findByIdEntryId(entryId);
        return identifications;
    }
}
