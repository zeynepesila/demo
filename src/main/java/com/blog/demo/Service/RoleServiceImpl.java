package com.blog.demo.Service;

import com.blog.demo.model.Role;
import com.blog.demo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository rolesRepository;

    @Override
    public List<Role> getAllRoles() {
        return rolesRepository.findAll();
    }

    @Override
    public Role getRoleById(UUID id) {
        return rolesRepository.findById(id).orElse(null);
    }

    // Burada metot ismi ve dönüş tipi yanlış yazılmıştı.
    @Override
    public Role createRole(Role role) {
        return rolesRepository.save(role);
    }

    @Override
    public void deleteRole(UUID id) {
        rolesRepository.deleteById(id);
    }
}