package com.natura.web.server.mapper;

import com.natura.web.server.model.Phenology;
import com.natura.web.server.model.Species;
import com.natura.web.server.persistence.database.entity.PhenologyEntity;
import org.mapstruct.Mapper;
import com.natura.web.server.persistence.database.entity.SpeciesEntity;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface SpeciesMapper {

    @Mapping(source = "phenologies", target = "phenologies", qualifiedByName = "phenologyEntityToPhenology")
    Species map(SpeciesEntity entity);

    @Mapping(source = "phenologies", target = "phenologies", qualifiedByName = "phenologyToPhenologyEntity")
    SpeciesEntity map(Species species);

    default Optional<Species> map(Optional<SpeciesEntity> entity) {
        return entity.map(this::map);
    }

    List<Species> map(List<SpeciesEntity> entities);
    @Named("phenologyToPhenologyEntity")
    static List<PhenologyEntity> phenologyToEntity(List<Phenology> phenologies) {
        List<PhenologyEntity> entities = new ArrayList<>();
        for (Phenology phenology: phenologies) {
            PhenologyEntity entity = new PhenologyEntity();
            entity.setId(phenology.getId());
            entity.setStart(phenology.getStart());
            entity.setEnd(phenology.getEnd());
            entities.add(entity);
        }
        return entities;
    }

    @Named("phenologyEntityToPhenology")
    static List<Phenology> entityToPhenology(List<PhenologyEntity> entities) {
        List<Phenology> phenologies = new ArrayList<>();
        for (PhenologyEntity entity: entities) {
            Phenology phenology = new Phenology(entity.getStart(), entity.getEnd());
            phenology.setId(entity.getId());
            phenologies.add(phenology);
        }
        return phenologies;
    }
}
