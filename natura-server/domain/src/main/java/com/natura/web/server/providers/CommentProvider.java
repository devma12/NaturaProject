package com.natura.web.server.providers;

import com.natura.web.server.model.Comment;
import com.natura.web.server.model.Identification;

import java.util.List;

public interface CommentProvider {
    Comment save(Comment comment);

    List<Comment> getCommentsByIdentification(Identification identification);
}
