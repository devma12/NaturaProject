package com.natura.web.server.mappers;

import com.natura.web.server.ports.database.entities.UserEntity;
import com.natura.web.server.model.User;
import org.mapstruct.Mapper;

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
