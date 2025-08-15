
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
@RequestMapping("/api/admin") // Bu controller'daki tüm endpoint'ler /api/admin ile başlar
public class AdminController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping("/posts")
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable UUID id) {
        postRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable UUID id) {
        commentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}