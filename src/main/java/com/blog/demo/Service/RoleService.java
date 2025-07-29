package com.blog.demo.Service;

import com.blog.demo.model.Role;
import java.util.List;
import java.util.UUID;

public interface RoleService {
    List<Role> getAllRoles();
    Role getRoleById(UUID id);
    Role createRole(Role role);
    void deleteRole(UUID id);
}
