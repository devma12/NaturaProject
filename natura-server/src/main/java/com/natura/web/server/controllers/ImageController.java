package com.natura.web.server.controllers;

import com.natura.web.server.entities.Image;
import com.natura.web.server.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping(path="/natura-api/image")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping(path="/upload")
    public @ResponseBody Image uploadImage(@RequestBody MultipartFile file) throws IOException {

        return imageService.upload(file);
    }

    @GetMapping(path="/get/{id}")
    public @ResponseBody Image download(@PathVariable("id") String imageId) {

        Long id = Long.parseLong(imageId);
        return imageService.download(id);
    }

}
