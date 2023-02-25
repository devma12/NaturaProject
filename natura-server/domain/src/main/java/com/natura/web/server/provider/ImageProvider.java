package com.natura.web.server.provider;

import com.natura.web.server.model.Image;

import java.util.Optional;

public interface ImageProvider {
    Optional<Image> getImageById(Long id);

    Image save(Image image);
}
