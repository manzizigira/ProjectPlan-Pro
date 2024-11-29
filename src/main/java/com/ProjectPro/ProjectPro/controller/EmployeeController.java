package com.ProjectPro.ProjectPro.controller;

import com.ProjectPro.ProjectPro.entity.Employee;
import com.ProjectPro.ProjectPro.entity.User;
import com.ProjectPro.ProjectPro.service.EmployeeService;
import com.ProjectPro.ProjectPro.service.UsersService;
import jakarta.servlet.http.HttpSession;
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

    @GetMapping("/supervisorList")
    public String employeePage(Model model, HttpSession session){

        Integer userId = (Integer) session.getAttribute("userId");

        String departments = employeeService.findDepartmentByUserId(userId);
        List<Employee> findSupervisor = employeeService.findSupervisorsByDepartment(departments);

        model.addAttribute("supervisors", findSupervisor);
        model.addAttribute("users", usersService.findAll());
        return "employee/supervisorList";

    }

    @GetMapping("/employeeListPage")
    public String employeeListPage(Model model, HttpSession session, String department){
        Integer userId = (Integer) session.getAttribute("userId");
        String departments = employeeService.findDepartmentByUserId(userId);
        List<Employee> findEmployee = employeeService.getEmployeesByDepartment(departments);
        model.addAttribute("employees", findEmployee);
        model.addAttribute("users", usersService.findAll());

        return "employee/employeeList";

    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> pathPage(@PathVariable int id){
        Employee employee = employeeService.findById(id);
        Map<String, Object> response = new HashMap<>();
        response.put("id", employee.getId());
        response.put("name", employee.getName());
        response.put("contractYears", employee.getContractYears());
        response.put("department", employee.getDepartment());
        response.put("employeeType", employee.getEmployeeType());
        response.put("email", employee.getUsers().getEmail());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/save")
    public String savePage(
            @RequestParam("userId") int userId,
            @RequestParam("name") String name,
            @RequestParam("contractYears") String contractYears,
            @RequestParam("department") String department,
            @RequestParam("employeeType") String employeeType
    ){
        User user = usersService.findById(userId);

        Employee employee = new Employee();

        employee.setName(name);
        employee.setContractYears(contractYears);
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
            existingEmployee.setContractYears(employee.getContractYears());
            existingEmployee.setDepartment(employee.getDepartment());
            existingEmployee.setEmployeeType(employee.getEmployeeType());
            employeeService.save(existingEmployee);
        }
        return "redirect:/employee/employeeListPage";
    }


    @PostMapping("/delete")
    public String deletePage(@RequestParam("employeeId") int id){
        employeeService.delete(id);
        return "redirect:/employee/employeePage";
    }


}
