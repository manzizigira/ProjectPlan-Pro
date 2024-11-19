package com.ProjectPro.ProjectPro.service;

import com.ProjectPro.ProjectPro.entity.Grading;
import com.ProjectPro.ProjectPro.entity.Role;
import com.ProjectPro.ProjectPro.entity.User;

import java.util.List;

public interface RoleService {

    Role save(Role role);

    void delete(int theId);

    List<Role> findAll();

    Role findById(int theId);

    Role findByRoleName(String roleName);

    public List<User> findSupervisors();
    public User findSupervisorForUser(User user);

    public boolean isSupervisor(User user);
}
