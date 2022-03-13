package com.natura.web.server.ports.database;

import com.natura.web.server.mappers.CommentMapper;
import com.natura.web.server.mappers.IdentificationMapper;
import com.natura.web.server.model.Comment;
import com.natura.web.server.model.Identification;
import com.natura.web.server.providers.CommentProvider;
import com.natura.web.server.ports.database.repo.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DatabaseCommentProvider implements CommentProvider {

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    IdentificationMapper identificationMapper;

    @Autowired
    CommentRepository commentRepository;

    @Override
    public Comment save(Comment comment) {
        return commentMapper.map(commentRepository.save(commentMapper.map(comment)));
    }

    @Override
    public List<Comment> getCommentsByIdentification(Identification identification) {
        return commentMapper.map(commentRepository.findByIdentification(identificationMapper.map(identification)));
    }
}
