package com.ProjectPro.ProjectPro.controller;

import com.ProjectPro.ProjectPro.Dto.ActivityDetailDto;
import com.ProjectPro.ProjectPro.Dto.EmployeeDto;
import com.ProjectPro.ProjectPro.Dto.TaskDetailsDto;
import com.ProjectPro.ProjectPro.entity.*;
import com.ProjectPro.ProjectPro.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class EmployeePageController {

    private TaskManagementService taskService;
    private DirectorateService directorateService;
    private UsersService usersService;
    private EmployeeService employeeService;
    private ReportService reportService;
    private ActivityService activityService;

    private static final String UPLOADED_FOLDER = "uploads/";

    @Autowired
    public EmployeePageController(TaskManagementService taskService, DirectorateService directorateService, UsersService usersService, EmployeeService employeeService, ReportService reportService, ActivityService activityService) {
        this.taskService = taskService;
        this.directorateService = directorateService;
        this.usersService = usersService;
        this.employeeService = employeeService;
        this.reportService = reportService;
        this.activityService = activityService;
    }

    @PostMapping("/submitReport")
    @ResponseBody
    public ResponseEntity<?> submitReport(
            @RequestParam("userId") Integer userId,
            @RequestParam("taskId") Integer taskId,
            @RequestParam("reportFile") MultipartFile reportFile,
            @RequestParam("progressPercentage") Integer progressPercentage,
            @RequestParam("reportDescription") String reportDescription) {

        User user = usersService.findById(userId);
        Employee employee = employeeService.findByUser(user);
        TaskManagement task = taskService.findById(taskId);

        // Fetch the latest report for the task by this employee
        Report latestReport = reportService.findLatestReportByTaskAndEmployee(task, employee);

        if (latestReport != null) {
            // Validate the progress percentage
            if (progressPercentage < latestReport.getProgressPercentage()) {
                return ResponseEntity.badRequest().body(
                        "You cannot submit a progress percentage lower than your previous report ("
                                + latestReport.getProgressPercentage() + "%).");
            }
        }

        String fileName = System.currentTimeMillis() + "_" + reportFile.getOriginalFilename();
        String filePath = "reports/" + fileName;
        File targetFile = new File("C:/Users/amine/Downloads/Jane/" + filePath);

        try {
            reportFile.transferTo(targetFile);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed.");
        }

        // Save the new report
        Report report = new Report();
        report.setEmployee(employee);
        report.setTaskManagement(task);
        report.setReportFile(filePath);
        report.setProgressPercentage(progressPercentage);
        report.setReportDescription(reportDescription);
        report.setSubmissionDate(new Date());

        reportService.save(report);

        return ResponseEntity.ok("Report submitted successfully.");
    }

    @PostMapping("/submitActivityReport")
    public String submitActivityReport(
            @RequestParam("userId") Integer userId,
            @RequestParam("activityId") Integer activityId,
            @RequestParam("submissionDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date submissionDate,
            @RequestParam("reportFile") MultipartFile reportFile,
            @RequestParam("progressPercentage") Integer progressPercentage,
            @RequestParam("reportDescription") String reportDescription,
            Model model) {

        User user = usersService.findById(userId);
        Employee employee = employeeService.findByUser(user);
        Activity activity = activityService.findById(activityId);

        String fileName = System.currentTimeMillis() + "_" + reportFile.getOriginalFilename();
        String filePath = "reports/" + fileName;
        File targetFile = new File("C:/Users/amine/Downloads/Jane/" + filePath);

        try {
            reportFile.transferTo(targetFile);
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("message", "File upload failed");
            return "error-page";
        }

        Report report = new Report();
        report.setEmployee(employee);
        report.setActivity(activity);
        report.setReportFile(filePath);
        report.setProgressPercentage(progressPercentage);
        report.setReportDescription(reportDescription);
        report.setSubmissionDate(submissionDate);

        reportService.save(report);

        return "redirect:/employee-home";
    }

    @GetMapping("/myTask/{theId}")
    @ResponseBody
    public List<TaskManagement> getTasksByEmployee(@PathVariable("theId") int theId) {
        return taskService.findTaskManagementIdByEmployeesId(theId);
    }

    @GetMapping("/api/directorates")
    @ResponseBody
    public ResponseEntity<List<Directorate>> getObjectives() {
        List<Directorate> directories = directorateService.findAll();
        return ResponseEntity.ok(directories);
    }

    @GetMapping("/api/tasks")
    @ResponseBody
    public ResponseEntity<List<TaskDetailsDto>> getTasks() {
        List<TaskManagement> tasks = taskService.findAll();
        List<TaskDetailsDto> taskDtos = tasks.stream()
                .map(task -> {
                    TaskDetailsDto dto = new TaskDetailsDto();
                    dto.setTaskId(task.getId());
                    dto.setTaskName(task.getName());
                    dto.setTaskLeader(task.getTaskLeader().getName());
                    dto.setEndDate(task.getEndDate());
                    dto.setCompleted(task.getCompleted());
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(taskDtos);
    }



    @GetMapping("/api/task-details/{taskId}")
    @ResponseBody
    public ResponseEntity<TaskDetailsDto> getTaskDetails(@PathVariable int taskId) {
        TaskManagement task = taskService.findById(taskId);
        if (task == null) {
            return ResponseEntity.notFound().build();
        }
        TaskDetailsDto taskDetails = new TaskDetailsDto();
        taskDetails.setTaskId(task.getId());
        taskDetails.setTaskName(task.getName());
        taskDetails.setActivityName(task.getActivities().getActivityName());
        taskDetails.setTaskLeader(task.getTaskLeader().getName());

        if (task.getProject() != null) {
            taskDetails.setProjectName(task.getProject().getName());
            if (task.getProject().getDirectorate() != null) {
                taskDetails.setObjectiveTitle(task.getProject().getDirectorate().getName());
            }
        }

        return ResponseEntity.ok(taskDetails);
    }

    @GetMapping("/api/activity-details/{activityId}")
    @ResponseBody
    public ResponseEntity<ActivityDetailDto> getActivityDetails(@PathVariable int activityId) {
        Activity activity = activityService.findById(activityId);
        if (activity == null) {
            return ResponseEntity.notFound().build();
        }

        ActivityDetailDto activityDetailDto = new ActivityDetailDto();
        activityDetailDto.setActivityId(activity.getId());
        activityDetailDto.setActivityName(activity.getActivityName());
        activityDetailDto.setActivityNotes(activity.getNotes());

        if (activity.getProject() != null) {
            activityDetailDto.setProjectName(activity.getProject().getName());
            if (activity.getProject().getDirectorate() != null) {
                activityDetailDto.setObjectiveTitle(activity.getProject().getDirectorate().getName());
            }
        }

        return ResponseEntity.ok(activityDetailDto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/updateTaskStatus")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateTaskStatus(@RequestBody Map<String, Integer> requestData) {
        Integer taskId = requestData.get("id");
        TaskManagement task = taskService.findById(taskId);

        if (task == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "Task not found.");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        // Check if the task has not started yet
        if (!task.getStarted()) {
            // Set start time and mark task as started
            task.setStartTime(LocalDateTime.now());
            task.setStarted(true);
        }

        // Update the task status to "In Progress"
        task.setStatus("In Progress");
        taskService.save(task); // Assuming your service has a save method for updating the task

        // Return a success response
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/updateActivityStatus")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateActivityStatus(@RequestBody Map<String, Integer> requestData) {
        Integer activityId = requestData.get("id");
        Activity activity = activityService.findById(activityId);

        if (activity == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "Task not found.");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        // Check if the activity has not started yet
        if (!activity.getStarted()) {
            // Set start time and mark activity as started
            activity.setStartTime(LocalDateTime.now());
            activity.setStarted(true);
        }

        // Update the activity status to "In Progress"
        activity.setStatus("In Progress");
        activityService.save(activity); // Assuming your service has a save method for updating the activity

        // Return a success response
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/employees")
    @ResponseBody
    public ResponseEntity<Map<String, List<EmployeeDto>>> getAllEmployees() {
        List<Employee> employees = employeeService.findAll();

        // Map Employee to EmployeeDTO to avoid circular references
        List<EmployeeDto> employeeDTOs = employees.stream()
                .map(employee -> new EmployeeDto(employee.getId(), employee.getName()))
                .collect(Collectors.toList());

        Map<String, List<EmployeeDto>> response = new HashMap<>();
        response.put("employees", employeeDTOs);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/employees/{id}")
    @ResponseBody
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable int id) {
        Employee employee = employeeService.findById(id);
        if (employee == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        EmployeeDto employeeDto = new EmployeeDto(employee.getId(), employee.getName());
        return ResponseEntity.ok(employeeDto);
    }


    @PutMapping("/api/tasks/{taskId}/escalate")
    public ResponseEntity<TaskManagement> escalateTask(
            @PathVariable int taskId,
            @RequestBody Map<String, Integer> requestBody) {
        Integer employeeId = requestBody.get("employeeId");
        if (employeeId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);  // Invalid input
        }

        return taskService.escalateTask(taskId, employeeId);
    }

    @PutMapping("/api/activities/{activityId}/escalate")
    public ResponseEntity<Activity> escalateActivity(
            @PathVariable int activityId,
            @RequestBody Map<String, Integer> requestBody) {
        Integer employeeId = requestBody.get("employeeId");
        if (employeeId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);  // Invalid input
        }

        return activityService.escalateActivity(activityId, employeeId);
    }

}
