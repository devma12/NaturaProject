package com.natura.web.server.mappers;

import com.natura.web.server.ports.database.entities.ImageEntity;
import com.natura.web.server.model.Image;
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
