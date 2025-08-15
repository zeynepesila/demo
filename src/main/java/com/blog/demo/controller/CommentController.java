package com.blog.demo.controller;

import com.blog.demo.model.Comment;
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
    public ResponseEntity<List<Comment>> getCommentsByPostId(@PathVariable UUID postId) {
        List<Comment> comments = commentRepository.findByPostPostId(postId);
        return ResponseEntity.ok(comments);
    }

    // Belirli bir posta yorum ekle
    @PostMapping("/{postId}/comments")
    public ResponseEntity<?> addCommentToPost(@PathVariable UUID postId, @RequestBody Comment newComment) {
        return postRepository.findById(postId)
                .map(post -> {
                    newComment.setPost(post);
                    commentRepository.save(newComment);
                    return ResponseEntity.ok(newComment);
                })
                .orElseGet(() -> ResponseEntity.notFound().build()); // ResourceNotFoundException yoksa böyle
    }
}




