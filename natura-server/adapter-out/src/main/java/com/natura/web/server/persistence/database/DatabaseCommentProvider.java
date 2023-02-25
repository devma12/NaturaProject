package com.natura.web.server.persistence.database;

import com.natura.web.server.mapper.CommentMapper;
import com.natura.web.server.model.Comment;
import com.natura.web.server.model.Identification;
import com.natura.web.server.provider.CommentProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.natura.web.server.mapper.IdentificationMapper;
import com.natura.web.server.persistence.database.repository.CommentRepository;

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
