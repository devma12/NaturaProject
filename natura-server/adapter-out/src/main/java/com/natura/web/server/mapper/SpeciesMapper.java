package com.natura.web.server.mapper;

import com.natura.web.server.model.Species;
import org.mapstruct.Mapper;
import com.natura.web.server.persistence.database.entity.SpeciesEntity;

import java.util.List;
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

    List<Species> map(List<SpeciesEntity> entities);
}
