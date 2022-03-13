package com.natura.web.server.mappers;

import com.natura.web.server.ports.database.entities.IdentificationEntity;
import com.natura.web.server.model.Identification;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring", uses = EntryMapper.class)
public interface IdentificationMapper {

    Identification map(IdentificationEntity entity);

    IdentificationEntity map(Identification identification);

    List<Identification> map(List<IdentificationEntity> entities);

    default Optional<Identification> map(Optional<IdentificationEntity> entity) {
        if (entity.isPresent()) {
            return Optional.of(map(entity.get()));
        } else {
            return Optional.empty();
        }
    }
}
