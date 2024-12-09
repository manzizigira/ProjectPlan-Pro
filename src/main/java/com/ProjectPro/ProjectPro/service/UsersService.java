package com.ProjectPro.ProjectPro.service;


import com.ProjectPro.ProjectPro.entity.*;

import java.util.List;

public interface UsersService {

    User save(User user);

    User findById(int theId);

    void delete(int theId);

    List<User> findAll();

    void assignRoleToUser(int userId, int roleId);

    public User validateUser(String username, String password);

    User findByUsername(String username);

    public List<TaskManagement> getTasksForLoggedInUser(int userId);
    public Activity getActivityForLoggedInUser(int userId);

    public boolean hasRole(User user, String roleName);
    List<Employee> findAllEmployees();
    User getEmployee (Employee employee);

    User getUsersByMessageSupervisor(List<MessageModel> messageModel);
}
