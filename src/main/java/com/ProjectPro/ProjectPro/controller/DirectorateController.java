package com.ProjectPro.ProjectPro.controller;
import com.ProjectPro.ProjectPro.entity.Directorate;
import com.ProjectPro.ProjectPro.entity.Employee;
import com.ProjectPro.ProjectPro.entity.ImplementingAgency;
import com.ProjectPro.ProjectPro.entity.Project;
import com.ProjectPro.ProjectPro.service.DirectorateService;
import com.ProjectPro.ProjectPro.service.EmployeeService;
import com.ProjectPro.ProjectPro.service.ProjectService;
import com.ProjectPro.ProjectPro.service.ImplementingAgencyService;
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

    private EmployeeService employeeService;

    @Autowired
    public DirectorateController(DirectorateService directorateService, ProjectService projectService, ImplementingAgencyService implementingAgencyService, EmployeeService employeeService) {
        this.directorateService = directorateService;
        this.projectService = projectService;
        this.implementingAgencyService = implementingAgencyService;
        this.employeeService = employeeService;
    }

    // Show Directorate Page
    @GetMapping("/directoratePage")
    public String showDirectoratePage(Model model) {

        // get the directories from db
        List<Directorate> directories = directorateService.findAll();

        // add to spring model
        model.addAttribute("directories", directories);
        model.addAttribute("employees", employeeService.findAll());
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

        // Assuming `Directorate` has methods `getId()`, `getName()`, `getCreatedAt()`, `getUpdatedAt()`, and `getDescription()`
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
    public String addDirectorate(@ModelAttribute("directorate") Directorate directorate) {
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
    public String addProject(@RequestParam("directorateId") int directorateId,
                             @RequestParam("name") String name,
                             @RequestParam("category") String category,
                             @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
                             @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
                             @RequestParam("description") String description,
                             @RequestParam(value = "projectManagerId") Integer projectManagerId,
                             Model model) {

        // Retrieve the directorate by id
        Directorate directorate = directorateService.findById(directorateId);

        // Create and save the new project
        Project newProject = new Project();
        newProject.setName(name);
        newProject.setCategory(category);
        newProject.setStartDate(startDate);
        newProject.setEndDate(endDate);
        newProject.setDescription(description);
        newProject.setDirectorate(directorate);

        if (projectManagerId != null){
            Employee projectManager = employeeService.findById(projectManagerId);
            newProject.setProjectManager(projectManager);
        }

        // Save the new project
        projectService.save(newProject);

        // Redirect to the directorates page or wherever appropriate
        return "redirect:/directoratePage";
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
