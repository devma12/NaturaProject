package com.natura.web.server.controllers;

import com.natura.web.server.entities.Identification;
import com.natura.web.server.exceptions.ServerException;
import com.natura.web.server.repo.IdentificationRepository;
import com.natura.web.server.services.IdentificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(path="/natura-api/identification")
public class IdentificationController {

    @Autowired
    private IdentificationRepository identificationRepository;

    @Autowired
    private IdentificationService identificationService;

    @GetMapping(path="/entry/{id}")
    public @ResponseBody
    List<Identification> getByEntry(@PathVariable("id") String id) throws IOException {

        Long entryId = Long.parseLong(id);
        List<Identification> identifications = identificationRepository.findByIdEntryId(entryId);
        return identifications;
    }

    @PostMapping(path="/new")
    public @ResponseBody
    Identification identify(@RequestParam("entryId") String entry,
                            @RequestParam("speciesId") String species,
                            @RequestParam("userId") String proposer) throws IOException {

        Long entryId = Long.parseLong(entry);
        Long speciesId = Long.parseLong(species);
        Long userId = Long.parseLong(proposer);
        try {
            return identificationService.identify(entryId, speciesId, userId);
        } catch (ServerException e) {
            throw e.responseStatus();
        }
    }

    @PostMapping(path="/validate")
    public @ResponseBody
    Identification validate(@RequestParam("entry") String entry,
                            @RequestParam("species") String species,
                            @RequestParam("validator") String validator) throws IOException {

        Long entryId = Long.parseLong(entry);
        Long speciesId = Long.parseLong(species);
        Long userId = Long.parseLong(validator);
        try {
            // validate identification
            Identification identification = identificationService.validate(entryId, speciesId, userId);

            // Check if proposer should become a validator
            identificationService.giveValidatorRights(identification.getSuggestedBy());

            return identification;
        } catch (ServerException e) {
            throw e.responseStatus();
        }
    }
}
