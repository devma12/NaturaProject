package com.natura.web.server.mappers;

import com.natura.web.server.ports.database.entities.EntryEntity;
import com.natura.web.server.ports.database.entities.FlowerEntity;
import com.natura.web.server.ports.database.entities.InsectEntity;
import com.natura.web.server.model.Entry;
import com.natura.web.server.model.Flower;
import com.natura.web.server.model.Insect;
import org.hibernate.MappingException;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface EntryMapper {

    Insect map(InsectEntity entity);

    Flower map(FlowerEntity entity);

    InsectEntity map(Insect insect);

    FlowerEntity map(Flower flower);

    List<Insect> mapInsectList(Iterable<InsectEntity> entities);

    List<Flower> mapFlowerList(Iterable<FlowerEntity> entities);

    default Entry mapEntryEntity(EntryEntity entity) {
        if (entity instanceof InsectEntity) {
            return map((InsectEntity) entity);
        } else if (entity instanceof FlowerEntity) {
            return map((FlowerEntity) entity);
        } else {
            throw new MappingException(String.format("Cannot mapping entry element of class %s", entity.getClass()));
        }
    }

    default EntryEntity mapEntry(Entry entry) {
        if (entry instanceof Insect) {
            return map((Insect) entry);
        } else if (entry instanceof Flower) {
            return map((Flower) entry);
        } else {
            throw new MappingException(String.format("Cannot mapping entry element of class %s", entry.getClass()));
        }
    }

    default Optional<Entry> map(Optional<EntryEntity> entity) {
        if (entity.isPresent()) {
            return Optional.of(mapEntryEntity(entity.get()));
        } else {
            return Optional.empty();
        }
    }

    default Optional<Flower> mapFlowerOptional(Optional<FlowerEntity> entity) {
        if (entity.isPresent()) {
            return Optional.of(map(entity.get()));
        } else {
            return Optional.empty();
        }
    }

    default Optional<Insect> mapInsectOptional(Optional<InsectEntity> entity) {
        if (entity.isPresent()) {
            return Optional.of(map(entity.get()));
        } else {
            return Optional.empty();
        }
    }
}
