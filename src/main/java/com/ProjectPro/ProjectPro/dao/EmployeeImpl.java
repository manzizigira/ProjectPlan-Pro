package com.ProjectPro.ProjectPro.dao;

import com.ProjectPro.ProjectPro.entity.Employee;
import com.ProjectPro.ProjectPro.entity.User;
import com.ProjectPro.ProjectPro.repository.EmployeeRepo;
import com.ProjectPro.ProjectPro.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeImpl implements EmployeeService {

    private EmployeeRepo employeeRepo;

    public EmployeeImpl(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    @Override
    public Employee save(Employee employee) {
        return employeeRepo.save(employee);
    }

    @Override
    public Employee findById(int theId) {
        Optional<Employee> result = employeeRepo.findById(theId);

        Employee employee = null;

        if(result.isPresent()){
            employee = result.get();
        }else{
            throw new RuntimeException("ID not found!");
        }
        return employee;
    }

    @Override
    public void delete(int theId) {
        employeeRepo.deleteById(theId);
    }

    @Override
    public List<Employee> findAll() {
        List<Employee> employees = employeeRepo.findAll();
        System.out.println("Number of employees retrieved: " + employees.size());
        for (Employee emp : employees) {
            System.out.println("Employee: " + emp.getName());
        }
        return employees;
    }

    @Override
    public Employee findByUser(User user) {
        return employeeRepo.findByUser(user);
    }

    @Override
    public List<Employee> findAllById(List<Integer> id) {
        return employeeRepo.findAllById(id);
    }

    @Override
    public Employee findByUserId(int userId) {
        return employeeRepo.findByUserId(userId);
    }

    @Override
    public String findDepartmentByUserId(int userId) {
        return employeeRepo.findDepartmentByUserId(userId);
    }

    @Override
    public List<Employee> getEmployeesByDepartment(String department) {
        return employeeRepo.getEmployeesByDepartment(department);
    }

    @Override
    public List<Employee> findSupervisorsByDepartment(String department) {
        return employeeRepo.findSupervisorsByDepartment(department);
    }

    @Override
    public List<Employee> findProjectManagersByDepartment(String department) {
        return employeeRepo.findProjectManagersByDepartment(department);
    }

    @Override
    public List<Employee> findEmployeesByDepartment(String department) {
        return employeeRepo.findEmployeesByDepartment(department);
    }


}
