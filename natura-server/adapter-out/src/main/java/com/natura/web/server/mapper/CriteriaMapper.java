package com.natura.web.server.mapper;

import com.natura.web.server.model.Criteria;
import com.natura.web.server.persistence.database.entity.CriteriaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CriteriaMapper {

    Criteria map(CriteriaEntity entity);

    CriteriaEntity map(Criteria criteria);
}
