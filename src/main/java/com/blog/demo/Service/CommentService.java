package com.blog.demo.Service;

import com.blog.demo.model.Comment;

import java.util.List;
import java.util.UUID;

public interface CommentService {
    List<Comment> getAllComments();
    Comment getCommentById(UUID id);
    Comment createComment(Comment comment);
    void deleteComment(UUID id);
}


