package com.blog.demo.controller;

import com.blog.demo.model.Role;
import com.blog.demo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/roles")

public class RoleController {
    @Autowired
    private RoleRepository rolesRepository;

    // Tüm rolleri getir
    @GetMapping
    public List<Role> getAllRoles() {
        return rolesRepository.findAll();
    }

    // ID'ye göre rol getir
    @GetMapping("/{id}")
    public Role getRoleById(@PathVariable UUID id) {
        return rolesRepository.findById(id).orElse(null);
    }

    // Yeni rol oluştur
    @PostMapping
    public Role createRole(@RequestBody Role role) {
        if (role.getRoleId() == null) {
            role.setRoleId(UUID.randomUUID());
        }
        return rolesRepository.save(role);
    }

    // Rol sil
    @DeleteMapping("/{id}")
    public void deleteRole(@PathVariable UUID id) {
        rolesRepository.deleteById(id);
    }
}
