package com.natura.web.server.persistence.database;


import com.natura.web.server.model.Image;
import com.natura.web.server.provider.ImageProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.natura.web.server.mapper.ImageMapper;
import com.natura.web.server.persistence.database.repository.ImageRepository;

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
