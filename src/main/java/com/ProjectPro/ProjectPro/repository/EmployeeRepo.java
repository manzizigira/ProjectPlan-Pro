package com.ProjectPro.ProjectPro.repository;

import com.ProjectPro.ProjectPro.entity.Employee;
import com.ProjectPro.ProjectPro.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepo extends JpaRepository<Employee,Integer> {
    Employee findByUser(User user);
    Employee findByUserId(int userId);
}
