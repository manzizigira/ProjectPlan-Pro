package com.ProjectPro.ProjectPro.dao;


import com.ProjectPro.ProjectPro.entity.Role;
import com.ProjectPro.ProjectPro.entity.User;
import com.ProjectPro.ProjectPro.repository.RoleRepo;
import com.ProjectPro.ProjectPro.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoleImpl implements RoleService {

    private RoleRepo roleRepo;

    public RoleImpl(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    @Override
    public Role save(Role role) {
        return roleRepo.save(role);
    }

    @Override
    public void delete(int theId) {
        roleRepo.deleteById(theId);
    }

    @Override
    public List<Role> findAll() {
        return roleRepo.findAll();
    }

    @Override
    public Role findById(int theId) {
        Optional<Role> roleOptional = roleRepo.findById(theId);

        Role role = null;

        if (roleOptional.isPresent()){
            role = roleOptional.get();
        }else{
            throw new RuntimeException("ID not Found");
        }
        return role;
    }

    @Override
    public Role findByRoleName(String roleName) {
        return roleRepo.findByRoleName(roleName);
    }

    @Override
    public List<User> findSupervisors() {
        Role supervisorRole = roleRepo.findByRoleName("SUPERVISOR");
        return new ArrayList<>(supervisorRole.getUsers());
    }

    @Override
    public User findSupervisorForUser(User user) {
        List<User> supervisors = findSupervisors();
        if (!supervisors.isEmpty()) {
            // Here, you can apply your logic if multiple supervisors exist, like assigning one based on the user.
            return supervisors.get(0);  // For simplicity, return the first supervisor
        }
        return null;
    }

    @Override
    public boolean isSupervisor(User user) {
        return user.getRoles().stream()
                .anyMatch(role -> role.getRoleName().equalsIgnoreCase("SUPERVISOR"));
    }
}
