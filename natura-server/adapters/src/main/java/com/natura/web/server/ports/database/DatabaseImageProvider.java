package com.natura.web.server.ports.database;


import com.natura.web.server.mappers.ImageMapper;
import com.natura.web.server.model.Image;
import com.natura.web.server.providers.ImageProvider;
import com.natura.web.server.ports.database.repo.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DatabaseImageProvider implements ImageProvider {

    @Autowired
    ImageMapper mapper;

    @Autowired
    ImageRepository imageRepository;

    @Override
    public Optional<Image> getImageById(Long id) {
        return mapper.map(imageRepository.findById(id));
    }

    @Override
    public Image save(Image image) {
        return mapper.map(imageRepository.save(mapper.map(image)));
    }
}
