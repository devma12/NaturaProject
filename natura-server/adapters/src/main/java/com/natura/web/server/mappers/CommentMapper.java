package com.natura.web.server.mappers;

import com.natura.web.server.ports.database.entities.CommentEntity;
import com.natura.web.server.model.Comment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {IdentificationMapper.class, EntryMapper.class, UserMapper.class})
public interface CommentMapper {

    Comment map(CommentEntity entity);

    CommentEntity map(Comment comment);

    List<Comment> map(List<CommentEntity> entities);
}
