package com.natura.web.server.mapper;

import com.natura.web.server.model.Image;
import com.natura.web.server.persistence.database.entity.ImageEntity;
import org.mapstruct.Mapper;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface ImageMapper {

    Image map(ImageEntity entity);

    ImageEntity map(Image image);

    default Optional<Image> map(Optional<ImageEntity> entity) {
        if (entity.isPresent()) {
            return Optional.of(map(entity.get()));
        } else {
            return Optional.empty();
        }
    }
}
