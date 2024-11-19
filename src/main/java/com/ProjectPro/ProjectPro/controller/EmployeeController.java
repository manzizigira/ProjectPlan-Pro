package com.ProjectPro.ProjectPro.controller;

import com.ProjectPro.ProjectPro.entity.Employee;
import com.ProjectPro.ProjectPro.entity.User;
import com.ProjectPro.ProjectPro.service.EmployeeService;
import com.ProjectPro.ProjectPro.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

    private EmployeeService employeeService;

    private UsersService usersService;

    @Autowired
    public EmployeeController(EmployeeService employeeService, UsersService usersService) {
        this.employeeService = employeeService;
        this.usersService = usersService;
    }

    @GetMapping("/employeePage")
    public String employeePage(Model model){

        model.addAttribute("employees", employeeService.findAll());
        model.addAttribute("users", usersService.findAll());

        return "employee/employeesPage";

    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> pathPage(@PathVariable int id){
        Employee employee = employeeService.findById(id);
        Map<String, Object> response = new HashMap<>();
        response.put("id", employee.getId());
        response.put("name", employee.getName());
        response.put("jobTitle", employee.getJobTitle());
        response.put("department", employee.getDepartment());
        response.put("employeeType", employee.getEmployeeType());
        response.put("email", employee.getUsers().getEmail());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/save")
    public String savePage(
            @RequestParam("userId") int userId,
            @RequestParam("name") String name,
            @RequestParam("jobTitle") String jobTitle,
            @RequestParam("department") String department,
            @RequestParam("employeeType") String employeeType
    ){
        User user = usersService.findById(userId);

        Employee employee = new Employee();

        employee.setName(name);
        employee.setJobTitle(jobTitle);
        employee.setDepartment(department);
        employee.setEmployeeType(employeeType);
        employee.setUsers(user);

        employeeService.save(employee);

        return "redirect:/employee/employeePage";
    }

    @PostMapping("/update")
    public String updateEmployee(@ModelAttribute Employee employee){
        Employee existingEmployee = employeeService.findById(employee.getId());
        if (existingEmployee != null) {
            existingEmployee.setName(employee.getName());
            existingEmployee.setJobTitle(employee.getJobTitle());
            existingEmployee.setDepartment(employee.getDepartment());
            existingEmployee.setEmployeeType(employee.getEmployeeType());
            employeeService.save(existingEmployee);
        }
        return "redirect:/employee/employeePage";
    }


    @PostMapping("/delete")
    public String deletePage(@RequestParam("employeeId") int id){
        employeeService.delete(id);
        return "redirect:/employee/employeePage";
    }


}
