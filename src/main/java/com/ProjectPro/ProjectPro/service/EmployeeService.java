package com.ProjectPro.ProjectPro.service;

import com.ProjectPro.ProjectPro.entity.Employee;
import com.ProjectPro.ProjectPro.entity.User;

import java.util.List;

public interface EmployeeService {

    Employee save(Employee employee);

    Employee findById(int theId);

    void delete(int theId);

    List<Employee> findAll();

    Employee findByUser(User user);

    List<Employee> findAllById(List<Integer> id);

}
