package com.ProjectPro.ProjectPro.controller;

import com.ProjectPro.ProjectPro.entity.Activity;
import com.ProjectPro.ProjectPro.entity.Employee;
import com.ProjectPro.ProjectPro.entity.TaskManagement;
import com.ProjectPro.ProjectPro.service.ActivityService;
import com.ProjectPro.ProjectPro.service.EmployeeService;
import com.ProjectPro.ProjectPro.service.TaskManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@Controller
@RequestMapping("/task")
public class TaskManagementController {

    private TaskManagementService taskManagementService;

    private EmployeeService employeeService;

    private ActivityService activityService;

    @Autowired
    public TaskManagementController(TaskManagementService taskManagementService, EmployeeService employeeService, ActivityService activityService) {
        this.taskManagementService = taskManagementService;
        this.employeeService = employeeService;
        this.activityService = activityService;
    }

    @GetMapping("/taskPage")
    public String taskPage(@RequestParam(value = "taskId", required = false) Integer taskId,Model model){
        List<Employee> employees = employeeService.findAll();
        model.addAttribute("tasks", taskManagementService.findAll());
        model.addAttribute("employees", employeeService.findAll());

        model.addAttribute("employeeCount", 1);

        if (taskId != null) {
            model.addAttribute("task", taskManagementService.findById(taskId));
        }

        model.addAttribute("activities", activityService.findAll());

        return "task/tasksPage";
    }

    @PostMapping("/taskPopulate") // Endpoint for processing the form
    public String processTaskPage(
            @RequestParam(value = "taskId", required = false) Integer taskId, // Optional taskId
            @RequestParam ("activityName") String activityName, // Single activity ID
            @RequestParam("employeeIds[]") List<String> employeeIds, // List of selected employee IDs
            @RequestParam(value = "taskLeaderId", required = false) Integer taskLeaderId, // Task Leader ID (New parameter)
            RedirectAttributes redirectAttributes) {

        // Validate inputs
        if (employeeIds.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Employees must be selected.");
            return "redirect:/task/taskPage"; // Redirect back to the task page or show an error
        }

        // Check if taskId is null
        if (taskId == null) {
            redirectAttributes.addFlashAttribute("error", "Task ID cannot be null.");
            return "redirect:/task/taskPage"; // Redirect back to the task page or show an error
        }

        // Fetch the existing TaskManagement object based on taskId
        TaskManagement taskManagement = taskManagementService.findById(taskId);
        if (taskManagement == null) {
            redirectAttributes.addFlashAttribute("error", "Task not found.");
            return "redirect:/task/taskPage"; // Redirect back to the task page or show an error
        }

        Activity activity = new Activity();
        activity.setActivityName(activityName);

        // Step 1: Assign the selected activity to the task (one-to-one relationship)
        taskManagement.setActivities(activity); // Assuming taskManagement has a setActivity method for the one-to-one relationship

        // Step 2: Clear the current employees from the task and activity
        taskManagement.getEmployees().clear(); // Clear employees from TaskManagement (many-to-many)
        activity.getEmployees().clear(); // Clear employees from Activity (many-to-many)

        // Step 3: Process the selected employee IDs and assign them to both the task and activity
        Employee taskLeader = null; // Declare a taskLeader variable
        for (String employeeId : employeeIds) {
            int id = Integer.parseInt(employeeId); // Convert String to int
            Employee employee = employeeService.findById(id);
            if (employee != null) {
                // Add the employee to both TaskManagement and Activity
                taskManagement.getEmployees().add(employee); // Add employee to TaskManagement
                activity.getEmployees().add(employee); // Add employee to Activity

                // Check if the current employee is the selected Task Leader
                if (id == taskLeaderId) {
                    taskLeader = employee; // Set this employee as the Task Leader
                }
            } else {
                redirectAttributes.addFlashAttribute("error", "Employee with ID " + id + " not found.");
                return "redirect:/task/taskPage"; // Redirect back to the task page or show an error
            }
        }

        // Step 4: Ensure Task Leader is one of the assigned employees
        if (taskLeader == null) {
            redirectAttributes.addFlashAttribute("error", "Task Leader must be one of the assigned employees.");
            return "redirect:/task/taskPage"; // Redirect back to the task page or show an error
        }

        // Step 5: Assign the Task Leader to TaskManagement
        taskManagement.setTaskLeader(taskLeader); // Assuming taskManagement has a setTaskLeader method

        // Step 6: Save the updated task and activity (since both relationships are bidirectional)
        taskManagementService.save(taskManagement); // Save TaskManagement with its employees and activity
        activityService.save(activity); // Save Activity with its updated employees

        // Flash success message
        redirectAttributes.addFlashAttribute("success", "Task and Activity updated and assigned to employees successfully, with Task Leader!");

        // Redirect back to the task page
        return "redirect:/task/taskPage";
    }






    @GetMapping("/employees")
    @ResponseBody // This indicates that the return value is written directly to the response body
    public List<Employee> getEmployees() {
        List<Employee> employees = employeeService.findAll();
        System.out.println("Fetched Employees: " + employees); // Debug log
        return employees;
    }

    @PostMapping("/update-task")
    public String updatePage(@ModelAttribute("taskId") TaskManagement taskManagement){
        taskManagementService.save(taskManagement);
        return "redirect:/task/taskPage";
    }


    @PostMapping("/delete")
    public String deletePage(@RequestParam("taskId") int id){
        taskManagementService.delete(id);
        return "redirect:/task/taskPage";
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> findByIdPage(@PathVariable int id){
        TaskManagement taskManagement = taskManagementService.findById(id);
        Map<String, Object> response = new HashMap<>();
        response.put("id", taskManagement.getId());
        response.put("name", taskManagement.getName());
        response.put("category", taskManagement.getCategory());
        response.put("startDate", taskManagement.getStartDate());
        response.put("endDate", taskManagement.getEndDate());
        response.put("notes", taskManagement.getNotes());
        response.put("status", taskManagement.getStatus());

        return ResponseEntity.ok(response);
    }


//    @PostMapping("/{taskId}/assign-activities")
//    public String assignActivitiesAndEmployees(@PathVariable("taskId") int taskId,
//                                               @RequestParam("activityIds") List<Integer> activityIds,
//                                               @RequestParam("employeeIds") List<Integer> employeeIds) {
//        // Call the service to assign activities and employees
//        taskManagementService.assignActivitiesAndEmployees(taskId, activityIds, employeeIds);
//
//        // Redirect back to the task page or some confirmation page
//        return "redirect:/task/taskPage";
//    }


}
