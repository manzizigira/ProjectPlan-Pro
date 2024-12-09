package com.ProjectPro.ProjectPro.controller;
import com.ProjectPro.ProjectPro.entity.*;
import com.ProjectPro.ProjectPro.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class DirectorateController {

    private DirectorateService directorateService;

    private ProjectService projectService;

    private ImplementingAgencyService implementingAgencyService;
    private RoleService roleService;

    private EmployeeService employeeService;
    private UsersService usersService;
    private MessageService messageService;

    @Autowired
    public DirectorateController(DirectorateService directorateService, ProjectService projectService, ImplementingAgencyService implementingAgencyService, RoleService roleService, EmployeeService employeeService, UsersService usersService, MessageService messageService) {
        this.directorateService = directorateService;
        this.projectService = projectService;
        this.implementingAgencyService = implementingAgencyService;
        this.roleService = roleService;
        this.employeeService = employeeService;
        this.usersService = usersService;
        this.messageService = messageService;
    }

    // Show Directorate Page
    @GetMapping("/directoratePage")
    public String showDirectoratePage(Model model, HttpSession session) {

        Integer userId = (Integer) session.getAttribute("userId");

        if (userId==null){
            return "redirect:/view";
        }

        // get the directories from session
        List<Directorate> directories = directorateService.findDirectoratesByUserId(userId);
        String departments = employeeService.findDepartmentByUserId(userId);
        List<Employee> findProjectManager = employeeService.findProjectManagersByDepartment(departments);

        // add to spring model
        model.addAttribute("directories", directories);
        model.addAttribute("employees", findProjectManager);
        model.addAttribute("agencies", implementingAgencyService.findAll());
        model.addAttribute("directorate", new Directorate());
        model.addAttribute("project", new Project());
        model.addAttribute("selectedDirectorate", new Directorate());

        return "directorates/directoratePage";
    }


    // link objectiveId with JS pop to update an Directorate
    @GetMapping("/directorate/{id}")
    public ResponseEntity<Map<String, Object>> getDirectorateById(@PathVariable int id) {
        Directorate directorate = directorateService.findById(id);

        // Assuming `Directorate` has methods `getId()`, `getName()`, `getStartDate()`, `getEndDate()`, and `getDescription()`
        Map<String, Object> response = new HashMap<>();
        response.put("id", directorate.getId());
        response.put("name", directorate.getName());
        response.put("startDate", directorate.getCreatedAt());
        response.put("endDate", directorate.getUpdatedAt());
        response.put("description", directorate.getDescription());

        return ResponseEntity.ok(response);
    }




    // Add Directorate
    @PostMapping("/add-directorate")
    public String addDirectorate(
                                    @RequestParam("name") String directorateName,
                                    @RequestParam("description") String description,
                                    @RequestParam("agency") Integer agency,
                                    HttpSession session

    ) {
        Directorate directorate = new Directorate();
        directorate.setName(directorateName);
        directorate.setDescription(description);
        if (agency != null) {
            ImplementingAgency implementingAgency = implementingAgencyService.findById(agency);
            directorate.setAgency(implementingAgency);
        }
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId != null){
            Employee employee = employeeService.findByUserId(userId);
            directorate.setHeadOfDirectorate(employee);
        }
        directorateService.save(directorate);
        return "redirect:/directoratePage";
    }


    //Update Directorate
    @PostMapping("/update")
    public String updateDirectorate(@ModelAttribute("directorateId") Directorate directorate) {
        directorateService.save(directorate);
        return "redirect:/directoratePage";
    }

    //Delete Directorate
    @PostMapping("/delete")
    public String deleteDirectorate(@RequestParam("directorateId") int id) {
        directorateService.delete(id);
        return "redirect:/directoratePage";
    }


    // save and associate a project with an objective
    @PostMapping("/add-project")
    public String addProject(
            @RequestParam("directorateId") int directorateId,
            @RequestParam("name") String name,
            @RequestParam("category") String category,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @RequestParam("description") String description,
            @RequestParam(value = "projectManagerId", required = false) Integer projectManagerId,
            HttpSession session,
            Model model) {

        // Retrieve the directorate by id
        Directorate directorate = directorateService.findById(directorateId);

        // Create a new project object
        Project newProject = new Project();
        newProject.setName(name);
        newProject.setCategory(category);
        newProject.setStartDate(startDate);
        newProject.setEndDate(endDate);
        newProject.setDescription(description);
        newProject.setDirectorate(directorate);

        // Save the new project first
        projectService.save(newProject);

        // Handle the project manager if provided
        if (projectManagerId != null) {
            Employee projectManager = employeeService.findById(projectManagerId);
            newProject.setProjectManager(projectManager);

            // Assuming Employee is related to User (either extends or has a reference)
            User projectManagerUser = projectManager.getUser();  // Assuming this method exists to get User object

            // Retrieve the HOD (current logged-in user)
            Integer userId = (Integer) session.getAttribute("userId");
            User hod = usersService.findById(userId);

            // Retrieve the Role for HOD and Project Manager
            Role hodRole = roleService.findByRoleName("HOD");
            Role projectManagerRole = roleService.findByRoleName("PROJECTMANAGER");

            // Create the message between HOD and PROJECTMANAGER about the project assignment
            messageService.createProjectAssignmentMessage(hod, projectManagerUser, newProject, hodRole, projectManagerRole);

        }

        // Redirect to the appropriate page (probably the directorate page or project details)
        return "redirect:/directoratePage";  // Modify this based on where you want to go after project assignment
    }





    //Add New Sub-Directorate and associate it with an Directorate
    @PostMapping("/add-implementing-agency")
    public String addImplementingAgency(@RequestParam("directorateId") int objectiveId,
                                  @RequestParam("name") String name,
                                  @RequestParam("description") String description) {
        // Find the Directorate by ID
        Directorate directorate = directorateService.findById(objectiveId);

        // Create and save the new ImplementingAgency
        ImplementingAgency implementingAgency = new ImplementingAgency();
        implementingAgency.setName(name);
        implementingAgency.setDescription(description);
        implementingAgency.setDirectory(directorate);
        implementingAgencyService.save(implementingAgency);

        return "redirect:/directoratePage";
    }

}
