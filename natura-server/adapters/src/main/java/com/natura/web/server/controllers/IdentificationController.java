package com.natura.web.server.controllers;

import com.natura.web.server.exceptions.DataNotFoundException;
import com.natura.web.server.exceptions.InvalidDataException;
import com.natura.web.server.exceptions.UserAccountException;
import com.natura.web.server.model.Comment;
import com.natura.web.server.model.Identification;
import com.natura.web.server.ports.database.entities.IdentificationEntity;
import com.natura.web.server.ports.database.repo.IdentificationRepository;
import com.natura.web.server.services.IdentificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(path = "/natura-api/identification")
public class IdentificationController {

    @Autowired
    private IdentificationRepository identificationRepository;

    @Autowired
    private IdentificationService identificationService;

    @GetMapping(path = "/entry/{id}")
    public @ResponseBody
    List<IdentificationEntity> getByEntry(@PathVariable("id") String id) {
        Long entryId = Long.parseLong(id);
        return identificationRepository.findByIdEntryId(entryId);
    }

    @PostMapping(path = "/new")
    @ResponseBody
    public Identification identify(@RequestParam("entryId") String entry,
                                   @RequestParam("speciesId") String species,
                                   @RequestParam("userId") String proposer)
            throws DataNotFoundException, InvalidDataException {
        Long entryId = Long.parseLong(entry);
        Long speciesId = Long.parseLong(species);
        Long userId = Long.parseLong(proposer);
        return identificationService.identify(entryId, speciesId, userId);
    }

    @PostMapping(path = "/validate")
    public @ResponseBody
    Identification validate(@RequestParam("entry") String entry,
                            @RequestParam("species") String species,
                            @RequestParam("validator") String validator)
            throws DataNotFoundException, UserAccountException.ValidationPermissionException, InvalidDataException {
        Long entryId = Long.parseLong(entry);
        Long speciesId = Long.parseLong(species);
        Long userId = Long.parseLong(validator);
        // validate identification
        Identification identification = identificationService.validate(entryId, speciesId, userId);

        // Check if proposer should become a validator
        identificationService.giveValidatorRights(identification.getSuggestedBy());

        return identification;
    }

    @PostMapping(path = "/comment")
    public @ResponseBody
    Comment comment(@RequestParam("entry") String entry,
                    @RequestParam("species") String species,
                    @RequestParam("observer") String observer,
                    @RequestParam("comment") String text)
            throws DataNotFoundException {
        Long entryId = Long.parseLong(entry);
        Long speciesId = Long.parseLong(species);
        Long userId = Long.parseLong(observer);
        // comment identification
        return identificationService.comment(entryId, speciesId, userId, text);
    }

    @GetMapping(path = "/comments")
    public @ResponseBody
    List<Comment> getComments(@RequestParam("entry") String entry,
                              @RequestParam("species") String species)
            throws DataNotFoundException {
        Long entryId = Long.parseLong(entry);
        Long speciesId = Long.parseLong(species);
        return identificationService.getIdentificationComments(entryId, speciesId);
    }

    @PutMapping(path = "/like")
    public @ResponseBody
    Identification like(@RequestParam("entry") String entry,
                        @RequestParam("species") String species,
                        @RequestParam("user") String user)
            throws DataNotFoundException {
        Long entryId = Long.parseLong(entry);
        Long speciesId = Long.parseLong(species);
        Long userId = Long.parseLong(user);
        return identificationService.like(entryId, speciesId, userId);
    }
}
