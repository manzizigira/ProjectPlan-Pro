package com.ProjectPro.ProjectPro.dao;

import com.ProjectPro.ProjectPro.entity.*;
import com.ProjectPro.ProjectPro.repository.ActivityRepo;
import com.ProjectPro.ProjectPro.repository.EmployeeRepo;
import com.ProjectPro.ProjectPro.repository.TaskManagementRepo;
import com.ProjectPro.ProjectPro.repository.UserRepo;
import com.ProjectPro.ProjectPro.service.RoleService;
import com.ProjectPro.ProjectPro.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserImpl implements UsersService {

    private UserRepo userRepo;
    private RoleService roleService;
    private TaskManagementRepo taskManagementRepo;
    private EmployeeRepo employeeRepo;
    private ActivityRepo activityRepo;

    @Autowired
    public UserImpl(UserRepo userRepo, RoleService roleService, TaskManagementRepo taskManagementRepo, EmployeeRepo employeeRepo, ActivityRepo activityRepo) {
        this.userRepo = userRepo;
        this.roleService = roleService;
        this.taskManagementRepo = taskManagementRepo;
        this.employeeRepo = employeeRepo;
        this.activityRepo = activityRepo;
    }

    @Override
    public User save(User user) {
        return userRepo.save(user);
    }

    @Override
    public User findById(int theId) {
        Optional<User> result = userRepo.findById(theId);
        User user = null;

        if (result.isPresent()){
            user = result.get();
        }else {
            throw new RuntimeException("Id not Found!");
        }
        return user;
    }

    @Override
    public void delete(int theId) {
        userRepo.deleteById(theId);
    }

    @Override
    public List<User> findAll() {
        return userRepo.findAll();
    }

    @Override
    public void assignRoleToUser(int userId, int roleId) {
        User user = this.findById(userId);
        Role role = roleService.findById(roleId);

        if (user == null) {
            throw new RuntimeException("User not found for ID: " + userId);
        }
        if (role == null) {
            throw new RuntimeException("Role not found for ID: " + roleId);
        }

        user.addRole(role);
        userRepo.save(user);
    }

    @Override
    public User validateUser(String username, String password) {
        User user = userRepo.findByEmail(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    @Override
    public User findByUsername(String username) {
        return userRepo.findByEmail(username);
    }

    @Override
    public List<TaskManagement> getTasksForLoggedInUser(int userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Employee employee = employeeRepo.findByUser(user);
        return taskManagementRepo.findByEmployees(employee);
    }

    @Override
    public Activity getActivityForLoggedInUser(int userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Employee employee = employeeRepo.findByUser(user);
        return activityRepo.findByEmployees(employee);
    }

    @Override
    public boolean hasRole(User user, String roleName) {
        List<Role> roles = user.getRoles(); // Assuming you have a getRoles() method in User
        return roles.stream()
                .anyMatch(role -> role.getRoleName().equals(roleName));
    }

    @Override
    public List<Employee> findAllEmployees() {
        return employeeRepo.findAll();
    }

    @Override
    public User getEmployee(Employee employee) {
        return userRepo.findByEmployee(employee);
    }

    @Override
    public User getUsersByMessageSupervisor(List<MessageModel> messageModel) {
        return userRepo.findUsersByMessageSupervisor(messageModel);
    }
}
