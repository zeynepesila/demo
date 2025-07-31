package com.blog.demo.controller;

import com.blog.demo.dto.RegisterRequest;
import com.blog.demo.model.Role;
import com.blog.demo.model.User;
import com.blog.demo.repository.RoleRepository;
import com.blog.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Normal kullanıcı girişi
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginUser) {
        User user = userRepository.findByEmail(loginUser.getEmail());

        if (user != null && passwordEncoder.matches(loginUser.getPasswordHash(), user.getPasswordHash())) {
            if ("ROLE_USER".equals(user.getRole().getName())) {
                String token = "user-token-" + user.getUserId();

                return ResponseEntity.ok(Collections.singletonMap("token", token));
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bu giriş sadece normal kullanıcılar içindir.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Geçersiz bilgiler");
        }
    }

    // Admin girişi
    @PostMapping("/admin/login")
    public ResponseEntity<?> adminLogin(@RequestBody User loginUser) {
        User user = userRepository.findByEmail(loginUser.getEmail());

        if (user != null && passwordEncoder.matches(loginUser.getPasswordHash(), user.getPasswordHash())) {
            if ("ROLE_ADMIN".equals(user.getRole().getName())) {
                String token = "admin-token-" + user.getUserId();
                return ResponseEntity.ok(Collections.singletonMap("token", token));
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Yönetici yetkisine sahip değilsiniz.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Geçersiz bilgiler");
        }
    }

    // Kayıt (sign up)
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email zaten kayıtlı");
        }

        User user = new User();
        user.setName(request.getUsername());
        user.setEmail(request.getEmail());

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        user.setPasswordHash(encodedPassword);

        Optional<Role> optionalRole = roleRepository.findByName("ROLE_USER");
        if (optionalRole.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Rol bulunamadı");
        }

        user.setRole(optionalRole.get());
        userRepository.save(user);

        return ResponseEntity.ok("Kayıt başarılı");
    }
}
