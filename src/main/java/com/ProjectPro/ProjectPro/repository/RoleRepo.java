package com.ProjectPro.ProjectPro.repository;

import com.ProjectPro.ProjectPro.entity.Role;
import com.ProjectPro.ProjectPro.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepo extends JpaRepository<Role, Integer> {
    Role findByRoleName(String roleName);

    List<Role> findByUsers(User user);
}
