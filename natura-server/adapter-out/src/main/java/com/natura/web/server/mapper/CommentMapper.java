package com.natura.web.server.mapper;

import com.natura.web.server.model.Comment;
import org.mapstruct.Mapper;
import com.natura.web.server.persistence.database.entity.CommentEntity;

import java.util.List;

@Mapper(componentModel = "spring", uses = {IdentificationMapper.class, EntryMapper.class, UserMapper.class})
public interface CommentMapper {

    Comment map(CommentEntity entity);

    CommentEntity map(Comment comment);

    List<Comment> map(List<CommentEntity> entities);
}
