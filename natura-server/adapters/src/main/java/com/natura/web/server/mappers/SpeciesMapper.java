package com.natura.web.server.mappers;

import com.natura.web.server.ports.database.entities.SpeciesEntity;
import com.natura.web.server.model.Species;
import org.mapstruct.Mapper;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface SpeciesMapper {

    Species map(SpeciesEntity entity);

    SpeciesEntity map(Species species);

    default Optional<Species> map(Optional<SpeciesEntity> entity) {
        if (entity.isPresent()) {
            return Optional.of(map(entity.get()));
        } else {
            return Optional.empty();
        }
    }
}
