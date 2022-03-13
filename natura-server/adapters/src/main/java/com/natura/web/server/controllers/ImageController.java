package com.natura.web.server.controllers;

import com.natura.web.server.exceptions.InvalidDataException;
import com.natura.web.server.model.Image;
import com.natura.web.server.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping(path = "/natura-api/image")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping(path = "/upload")
    public @ResponseBody
    Image uploadImage(@RequestBody MultipartFile file) throws InvalidDataException.InvalidFileException, IOException {
        return imageService.upload(file.getOriginalFilename(), file.getContentType(), file.getInputStream());
    }

    @GetMapping(path = "/get/{id}")
    public @ResponseBody
    Image download(@PathVariable("id") String imageId) {

        Long id = Long.parseLong(imageId);
        return imageService.download(id);
    }

}
