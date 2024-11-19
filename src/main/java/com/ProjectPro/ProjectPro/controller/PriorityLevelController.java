package com.ProjectPro.ProjectPro.controller;

import com.ProjectPro.ProjectPro.entity.Activity;
import com.ProjectPro.ProjectPro.entity.PriorityLevel;
import com.ProjectPro.ProjectPro.entity.Project;
import com.ProjectPro.ProjectPro.entity.TaskManagement;
import com.ProjectPro.ProjectPro.repository.ActivityRepo;
import com.ProjectPro.ProjectPro.repository.PriorityLevelRepo;
import com.ProjectPro.ProjectPro.repository.ProjectRepo;
import com.ProjectPro.ProjectPro.repository.TaskManagementRepo;
import com.ProjectPro.ProjectPro.service.ActivityService;
import com.ProjectPro.ProjectPro.service.PriorityLevelService;
import com.ProjectPro.ProjectPro.service.ProjectService;
import com.ProjectPro.ProjectPro.service.TaskManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.*;
import java.util.Map;

@Controller
@RequestMapping("/priority-levels")
public class PriorityLevelController {

    private PriorityLevelService priorityLevelService;

    private ProjectService projectService;

    private ProjectRepo projectRepo;

    private TaskManagementRepo taskManagementRepo;

    private TaskManagementService taskManagementService;

    private ActivityRepo activityRepo;

    private ActivityService activityService;

    private PriorityLevelRepo priorityLevelRepo;

    @Autowired
    public PriorityLevelController(PriorityLevelService priorityLevelService, ProjectService projectService, ProjectRepo projectRepo, TaskManagementRepo taskManagementRepo, TaskManagementService taskManagementService, ActivityRepo activityRepo, ActivityService activityService, PriorityLevelRepo priorityLevelRepo) {
        this.priorityLevelService = priorityLevelService;
        this.projectService = projectService;
        this.projectRepo = projectRepo;
        this.taskManagementRepo = taskManagementRepo;
        this.taskManagementService = taskManagementService;
        this.activityRepo = activityRepo;
        this.activityService = activityService;
        this.priorityLevelRepo = priorityLevelRepo;
    }

    // Display the table of Priority Levels and a form to select a project
    @GetMapping
    public String listPriorityLevels(Model model) {
        List<PriorityLevel> priorityLevels = priorityLevelService.getAllPriorityLevelsSorted();
        List<Project> projects = projectService.findAll();

            model.addAttribute("priorityLevels", priorityLevels);
        model.addAttribute("projects", projects);
        model.addAttribute("tasks", taskManagementService.findAll());
        model.addAttribute("activities", activityService.findAll());
        return "priorityL/priortyPage";
    }




    @GetMapping("/{projectId}")
    public ResponseEntity<Map<String, Object>> getPriorityLevelByProjectId(@PathVariable int projectId) {
        Project project = projectService.findById(projectId);
        PriorityLevel priorityLevel = project.getPriorityLevel();

        Map<String, Object> response = new HashMap<>();
        if (priorityLevel == null) {
            // Return an empty priority level with default values
            response.put("id", null);
            response.put("estimatedCostScore", null);
            response.put("communityBenefitScore", null);
            response.put("stakeholderEngagementScore", null);
            response.put("priorityValue", null);
        } else {
            // Fill in the response with the actual priority level data
            response.put("id", priorityLevel.getId());
            response.put("estimatedCostScore", priorityLevel.getEstimatedCost());
            response.put("communityBenefitScore", priorityLevel.getCommunityBenefitScore());
            response.put("stakeholderEngagementScore", priorityLevel.getStakeholderEngagementScore());
            response.put("priorityValue", priorityLevel.getPriorityValue());
        }

        return ResponseEntity.ok(response);
    }


    @GetMapping("/task/{taskId}")
    public ResponseEntity<Map<String, Object>> getPriorityLevelByTaskId(@PathVariable int taskId) {
        TaskManagement task = taskManagementService.findById(taskId); // Assuming you have a task service to fetch task details
        PriorityLevel priorityLevel = task.getPriorityLevel(); // Assuming Task has a method to get its PriorityLevel

        Map<String, Object> response = new HashMap<>();
        if (priorityLevel == null) {
            // Return an empty priority level with default values
            response.put("id", null);
            response.put("estimatedCostScore", null);
            response.put("communityBenefitScore", null);
            response.put("stakeholderEngagementScore", null);
            response.put("priorityValue", null);
        } else {
            // Fill in the response with the actual priority level data
            response.put("id", priorityLevel.getId());
            response.put("estimatedCostScore", priorityLevel.getEstimatedCost());
            response.put("communityBenefitScore", priorityLevel.getCommunityBenefitScore());
            response.put("stakeholderEngagementScore", priorityLevel.getStakeholderEngagementScore());
            response.put("priorityValue", priorityLevel.getPriorityValue());
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/activity/{activityId}")
    public ResponseEntity<Map<String, Object>> getPriorityLevelByActivityId(@PathVariable int activityId) {
        Activity activity = activityService.findById(activityId); // Fetch activity details using the service
        PriorityLevel priorityLevel = activity.getPriorityLevel(); // Assuming Activity has a method to get its PriorityLevel

        Map<String, Object> response = new HashMap<>();
        if (priorityLevel == null) {
            // Return an empty priority level with default values
            response.put("id", null);
            response.put("estimatedCostScore", null);
            response.put("communityBenefitScore", null);
            response.put("stakeholderEngagementScore", null);
            response.put("priorityValue", null);
        } else {
            // Fill in the response with the actual priority level data
            response.put("id", priorityLevel.getId());
            response.put("estimatedCostScore", priorityLevel.getEstimatedCost());
            response.put("communityBenefitScore", priorityLevel.getCommunityBenefitScore());
            response.put("stakeholderEngagementScore", priorityLevel.getStakeholderEngagementScore());
            response.put("priorityValue", priorityLevel.getPriorityValue());
        }

        return ResponseEntity.ok(response);
    }


    @GetMapping("/update/project")
    @Transactional
    public ResponseEntity<Map<String, Object>> updateProjectPriorityLevel(
            @RequestParam Integer projectId,
            @RequestParam("estimatedCost") double estimatedCost,
            @RequestParam("communityBenefitScore") double communityBenefitScore,
            @RequestParam("stakeholderEngagementScore") double stakeholderEngagementScore) {

        // Validate the project ID
        Optional<Project> projectOptional = projectRepo.findById(projectId);
        if (!projectOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Project not found"));
        }

        // Delete previous priority level if it exists
        priorityLevelRepo.deleteByProjectId(projectId);
        Project project = projectOptional.get();
        String predictedPriorityValue = priorityLevelService.getPrediction(
                estimatedCost, communityBenefitScore, stakeholderEngagementScore, project);

        return ResponseEntity.ok(Map.of("predicted_priority_value", predictedPriorityValue));
    }


    @GetMapping("/update/task")
    @Transactional
    public ResponseEntity<Map<String, Object>> updateTaskPriorityLevel(
            @RequestParam Integer taskId,
            @RequestParam("estimatedCost") double estimatedCost,
            @RequestParam("communityBenefitScore") double communityBenefitScore,
            @RequestParam("stakeholderEngagementScore") double stakeholderEngagementScore) {

        // Validate the task ID
        Optional<TaskManagement> taskOptional = taskManagementRepo.findById(taskId);
        if (!taskOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Task not found"));
        }

        // Delete previous priority level if it exists
        priorityLevelRepo.deleteByTaskManagementId(taskId);
        TaskManagement task = taskOptional.get();
        String predictedPriorityValue = priorityLevelService.getTaskPrediction(
                taskId, estimatedCost, communityBenefitScore, stakeholderEngagementScore, task);

        return ResponseEntity.ok(Map.of("predicted_priority_value", predictedPriorityValue));
    }

    @GetMapping("/update/activity")
    @Transactional
    public ResponseEntity<Map<String, Object>> updateActivityPriorityLevel(
            @RequestParam Integer activityId,
            @RequestParam("estimatedCost") double estimatedCost,
            @RequestParam("communityBenefitScore") double communityBenefitScore,
            @RequestParam("stakeholderEngagementScore") double stakeholderEngagementScore) {

        // Validate the activity ID
        Optional<Activity> activityOptional = activityRepo.findById(activityId);
        if (!activityOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Activity not found"));
        }

        // Delete previous priority level if it exists
        priorityLevelRepo.deleteByActivityId(activityId);
        Activity activity = activityOptional.get();
        String predictedPriorityValue = priorityLevelService.getActivityPrediction(
                activityId, estimatedCost, communityBenefitScore, stakeholderEngagementScore, activity);

        return ResponseEntity.ok(Map.of("Predicted Priority Value", predictedPriorityValue));
    }

    // Show the form for updating a priority level
    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") int id, Model model) {
        PriorityLevel priorityLevel = priorityLevelService.findById(id);
        if (priorityLevel == null) {
            model.addAttribute("error", "Priority Level not found.");
            return "redirect:/priority-levels";
        }

        model.addAttribute("priorityLevel", priorityLevel);
        model.addAttribute("projects", projectService.findAll());
        return "priorityLevel/update";
    }





    // Handle deletion of a priority level
    @GetMapping("/delete/{id}")
    public String deletePriorityLevel(@PathVariable("id") int id, Model model) {
        PriorityLevel priorityLevel = priorityLevelService.findById(id);
        if (priorityLevel == null) {
            model.addAttribute("error", "Priority Level not found.");
        } else {
            priorityLevelService.delete(id);
            model.addAttribute("message", "Priority level deleted successfully.");
        }
        return "redirect:/priority-levels";
    }

    @GetMapping("/predict-priority")
    public ResponseEntity<Map<String, Object>> predictPriority(
            @RequestParam("estimatedCost") double estimatedCost,
            @RequestParam("communityBenefitScore") double communityBenefitScore,
            @RequestParam("stakeholderEngagementScore") double stakeholderEngagementScore,
            @RequestParam("projectId") int projectId) {

        Optional<Project> projectOptional = projectRepo.findById(projectId);
        if (!projectOptional.isPresent()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Project not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        Project project = projectOptional.get();
        String prediction = priorityLevelService.getPrediction(estimatedCost, communityBenefitScore, stakeholderEngagementScore, project);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("predicted_priority_value", prediction);
        // Add any other relevant data to the response if needed
        return ResponseEntity.ok(responseData);
    }


    @GetMapping("/predict-priority-task")
    public ResponseEntity<Map<String, Object>> predictPriority(
            @RequestParam("taskId") int taskId,
            @RequestParam("estimatedCost") double estimatedCost,
            @RequestParam("communityBenefitScore") double communityBenefitScore,
            @RequestParam("stakeholderEngagementScore") double stakeholderEngagementScore) {

        // Check if the task exists
        Optional<TaskManagement> taskOptional = taskManagementRepo.findById(taskId);
        if (!taskOptional.isPresent()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Task not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        TaskManagement taskManagement = taskOptional.get();

        // Call the service method to get the prediction
        String predictedPriorityValue = priorityLevelService.getTaskPrediction(
                taskId, estimatedCost, communityBenefitScore, stakeholderEngagementScore, taskManagement);

        // Prepare the response data
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("predicted_priority_value", predictedPriorityValue);

        // Add any other relevant data to the response if needed
        return ResponseEntity.ok(responseData);
    }


    @GetMapping("/predict-priority-activity")
    public ResponseEntity<Map<String, Object>> predictPriorityActivity(
            @RequestParam("activityId") int activityId,
            @RequestParam("estimatedCost") double estimatedCost,
            @RequestParam("communityBenefitScore") double communityBenefitScore,
            @RequestParam("stakeholderEngagementScore") double stakeholderEngagementScore) {

        // Check if the activity exists
        Optional<Activity> activityOptional = activityRepo.findById(activityId);
        if (!activityOptional.isPresent()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Activity not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        Activity activity = activityOptional.get();

        // Call the service method to get the prediction
        String predictedPriorityValue = priorityLevelService.getActivityPrediction(
                activityId, estimatedCost, communityBenefitScore, stakeholderEngagementScore, activity);

        // Prepare the response data
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("predicted_priority_value", predictedPriorityValue);

        // Add any other relevant data to the response if needed
        return ResponseEntity.ok(responseData);
    }


}
