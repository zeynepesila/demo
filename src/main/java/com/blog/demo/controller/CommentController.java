package com.blog.demo.controller;

import com.blog.demo.model.Comment;
import com.blog.demo.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    @Autowired
    private CommentRepository commentRepository;

    // Tüm yorumları getir
    @GetMapping
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    // ID'ye göre yorumu getir
    @GetMapping("/{id}")
    public Comment getCommentById(@PathVariable UUID id) {
        return commentRepository.findById(id).orElse(null);
    }

    // Yeni yorum oluştur
    @PostMapping
    public Comment createComment(@RequestBody Comment comment) {
        if (comment.getCommentId() == null) {
            comment.setCommentId(UUID.randomUUID());
        }
        return commentRepository.save(comment);
    }

    // Yorumu sil
    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable UUID id) {
        commentRepository.deleteById(id);
    }
}

