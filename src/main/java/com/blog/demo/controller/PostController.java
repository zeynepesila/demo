package com.blog.demo.controller;

import com.blog.demo.dto.PostRequest;
import com.blog.demo.model.Post;
import com.blog.demo.model.User;
import com.blog.demo.repository.PostRepository;
import com.blog.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    // Tüm postları getir
    @GetMapping
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // Yeni post oluştur
    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody PostRequest postDto,
                                        @RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Geçersiz token");
        }

        String token = authHeader.substring(7); // "editor-token-UUID"
        String[] parts = token.split("-token-");
        if (parts.length != 2) {
            return ResponseEntity.status(401).body("Geçersiz token formatı");
        }

        UUID userId;
        try {
            userId = UUID.fromString(parts[1]);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body("Geçersiz kullanıcı ID");
        }

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Kullanıcı bulunamadı");
        }

        User author = userOpt.get();

        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setSummary(postDto.getSummary());
        post.setContent(postDto.getContent());
        post.setAuthor(author);

        postRepository.save(post);

        return ResponseEntity.ok("Post başarıyla kaydedildi");
    }

    // ID'ye göre post getir
    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable UUID id) {
        Optional<Post> postOpt = postRepository.findById(id);
        if (postOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Post bulunamadı");
        }
        return ResponseEntity.ok(postOpt.get());
    }
}
