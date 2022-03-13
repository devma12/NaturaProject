package com.natura.web.server.mappers;

import com.natura.web.server.model.Criteria;
import com.natura.web.server.ports.database.entities.CriteriaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CriteriaMapper {

    Criteria map(CriteriaEntity entity);

    CriteriaEntity map(Criteria criteria);
}
