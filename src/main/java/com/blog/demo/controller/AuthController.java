package com.blog.demo.controller;

import com.blog.demo.dto.LoginRequest;
import com.blog.demo.dto.LoginResponse;
import com.blog.demo.dto.RegisterRequest;
import com.blog.demo.model.Role;
import com.blog.demo.model.User;
import com.blog.demo.repository.RoleRepository;
import com.blog.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5174", allowCredentials = "true")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Giriş işlemi (tüm roller için)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail());

        if (user != null && passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
            String role = user.getRole().getName();
            String token = role.toLowerCase() + "-token-" + user.getUserId(); // örnek token

            LoginResponse response = new LoginResponse(token, role);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("message", "Geçersiz email veya şifre."));
        }
    }

    // Kayıt işlemi
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", "Email zaten kayıtlı."));
        }

        User user = new User();
        user.setName(request.getUsername());
        user.setEmail(request.getEmail());

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        user.setPasswordHash(encodedPassword);

        Optional<Role> optionalRole = roleRepository.findByName(request.getRole());
        if (optionalRole.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Rol bulunamadı."));
        }

        user.setRole(optionalRole.get());
        userRepository.save(user);

        return ResponseEntity.ok(Collections.singletonMap("message", "Kayıt başarılı."));
    }
}
