package com.natura.web.server.mapper;

import com.natura.web.server.model.User;
import org.mapstruct.Mapper;
import com.natura.web.server.persistence.database.entity.UserEntity;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User map(UserEntity entity);

    UserEntity map(User user);

    default Optional<User> map(Optional<UserEntity> entity) {
        if (entity.isPresent()) {
            return Optional.of(map(entity.get()));
        } else {
            return Optional.empty();
        }
    }
}
