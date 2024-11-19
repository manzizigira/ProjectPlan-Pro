package com.ProjectPro.ProjectPro.controller;


import com.ProjectPro.ProjectPro.entity.Activity;
import com.ProjectPro.ProjectPro.entity.Project;
import com.ProjectPro.ProjectPro.entity.TaskManagement;
import com.ProjectPro.ProjectPro.service.ActivityService;
import com.ProjectPro.ProjectPro.service.ProjectService;
import com.ProjectPro.ProjectPro.service.TaskManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/project")
public class ProjectController {

    private ProjectService projectService;

    private TaskManagementService taskManagementService;

    private ActivityService activityService;

    @Autowired
    public ProjectController(ProjectService projectService, TaskManagementService taskManagementService, ActivityService activityService) {
        this.projectService = projectService;
        this.taskManagementService = taskManagementService;
        this.activityService = activityService;
    }


    @GetMapping("/projectPage")
    public String projectPage(Model model){
        model.addAttribute("projects", projectService.findAll());

        return "project/projectPage";
    }

    @PostMapping("/add-project")
    public String saveProject(@ModelAttribute("projects")Project project){
        projectService.save(project);
        return "redirect:/project/projectPage";
    }

    @PostMapping("/update-project")
    public String updateProject(
            @RequestParam("projectId") int projectId,
            @RequestParam("name") String name,
            @RequestParam("category") String category,
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam("description") String description) {

        // Fetch the project by ID (assume projectService exists)
        Project project = projectService.findById(projectId);

        // Update project details
        project.setName(name);
        project.setCategory(category);
        project.setStartDate(startDate);
        project.setEndDate(endDate);
        project.setDescription(description);

        // Save the updated project (assume projectService exists)
        projectService.save(project);

        return "redirect:/project/projectPage"; // Redirect to projects page or any other suitable location
    }

    @PostMapping("/delete-project")
    public String deleteProject(@RequestParam("projectId") int id){
        projectService.delete(id);
        return "redirect:/project/projectPage";
    }

    @GetMapping("/projects/{id}")
    public ResponseEntity<Map<String, Object>> findByIdPage(@PathVariable int id){
        Project project =  projectService.findById(id);
        Map<String, Object> response = new HashMap<>();
        response.put("id", project.getId());
        response.put("name", project.getName());
        response.put("category", project.getCategory());
        response.put("startDate", project.getStartDate());
        response.put("endDate", project.getEndDate());
        response.put("description", project.getDescription());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add-task")
    public String addTaskPage(@RequestParam("projectId") int project,
                              @RequestParam("name") String name,
                              @RequestParam("category") String category,
                              @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                              @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                              @RequestParam("notes") String notes,
                              @RequestParam("status") String status) {

        Project project1 = projectService.findById(project);

        TaskManagement taskManagement = new TaskManagement();
        taskManagement.setName(name);
        taskManagement.setCategory(category);
        taskManagement.setStartDate(java.sql.Date.valueOf(startDate));
        taskManagement.setEndDate(java.sql.Date.valueOf(endDate));
        taskManagement.setNotes(notes);
        taskManagement.setStatus(status);
        taskManagement.setProject(project1);


        taskManagementService.save(taskManagement);

        return "redirect:/project/projectPage";
    }

    @PostMapping("/add-activity")
    public String addActivityPage(@RequestParam("projectId") int project,
                                  @RequestParam("activityName") String name,
                                  @RequestParam("notes") String notes,
                                  @RequestParam("status") String status){

        Project project1 = projectService.findById(project);

        Activity activity = new Activity();
        activity.setActivityName(name);
        activity.setNotes(notes);
        activity.setStatus("New");
        activity.setProject(project1);

        activityService.save(activity);

        return "redirect:/project/projectPage";
    }
}
