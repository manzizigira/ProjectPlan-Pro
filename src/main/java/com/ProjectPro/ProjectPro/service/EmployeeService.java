package com.ProjectPro.ProjectPro.service;

import com.ProjectPro.ProjectPro.entity.Employee;
import com.ProjectPro.ProjectPro.entity.User;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeService {

    Employee save(Employee employee);

    Employee findById(int theId);

    void delete(int theId);

    List<Employee> findAll();

    Employee findByUser(User user);

    List<Employee> findAllById(List<Integer> id);

    Employee findByUserId(int userId);

    String findDepartmentByUserId(@Param("userId") int userId);

    List<Employee> getEmployeesByDepartment(@Param("department") String department);

    List<Employee> findSupervisorsByDepartment(@Param("department") String department);

    List<Employee> findProjectManagersByDepartment(@Param("department") String department);

    List<Employee> findEmployeesByDepartment(@Param("department") String department);


}
