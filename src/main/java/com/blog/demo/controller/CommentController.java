package com.blog.demo.controller;

import com.blog.demo.model.Comment;
import com.blog.demo.model.Post;
import com.blog.demo.repository.CommentRepository;
import com.blog.demo.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    // Belirli bir postun yorumlarını getir
    @GetMapping("/{postId}/comments")
    public List<Comment> getCommentsByPostId(@PathVariable UUID postId) {
        return commentRepository.findByPostPostId(postId);
    }

    // Belirli bir posta yorum ekle
    @PostMapping("/{postId}/comments")
    public ResponseEntity<Comment> addCommentToPost(@PathVariable UUID postId, @RequestBody Comment comment) {
        return postRepository.findById(postId)
                .map(post -> {
                    comment.setPost(post);
                    if (comment.getCommentId() == null) {
                        comment.setCommentId(UUID.randomUUID());
                    }
                    commentRepository.save(comment);
                    return ResponseEntity.ok(comment);
                })
                .orElse(ResponseEntity.notFound().build());
    }




}

