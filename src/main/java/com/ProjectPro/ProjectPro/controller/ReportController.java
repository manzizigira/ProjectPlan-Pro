package com.ProjectPro.ProjectPro.controller;

import com.ProjectPro.ProjectPro.Dto.*;
import com.ProjectPro.ProjectPro.entity.*;
import com.ProjectPro.ProjectPro.repository.ReportRepo;
import com.ProjectPro.ProjectPro.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.MalformedURLException;


import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/reports-page")
public class ReportController {

    private ReportService reportService;

    private GradingService gradingService;

    private TaskManagementService taskManagementService;

    private ReportRepo reportRepo;

    private ActivityService activityService;

    private UsersService usersService;

    private EmployeeService employeeService;

    @Autowired
    public ReportController(ReportService reportService, GradingService gradingService, TaskManagementService taskManagementService, ReportRepo reportRepo, ActivityService activityService, UsersService usersService, EmployeeService employeeService) {
        this.reportService = reportService;
        this.gradingService = gradingService;
        this.taskManagementService = taskManagementService;
        this.reportRepo = reportRepo;
        this.activityService = activityService;
        this.usersService = usersService;
        this.employeeService = employeeService;
    }

    @GetMapping
    public String viewReports(Model model) {
        List<Report> reports = reportService.findAll().stream()
                .filter(report ->
                        (report.getTaskManagement() != null && !"Completed".equals(report.getTaskManagement().getStatus())) ||
                                (report.getActivity() != null && !"Completed".equals(report.getActivity().getStatus()))
                )
                .collect(Collectors.toList());
        int pendingCount = reportService.countPendingReports();
        int gradedCount = reportService.countGradedReports();
        List<Integer> pendingReportIds = reportService.findPendingReportIds();

        model.addAttribute("reports", reports);
        model.addAttribute("pendingCount", pendingCount);
        model.addAttribute("gradedCount", gradedCount);
        model.addAttribute("pendingReportIds", pendingReportIds);
        return "report/reports";
    }

    // HOD views Project Manager Reports
    @GetMapping("/hod-report")
    public String viewPendingProjectManagerReport(Model model){
        List<Map<String,Object>> pendingReports = reportService.findPendingReportsByProjectManagers();
        List<Report> reports = reportService.findAll().stream()
                .filter(report ->
                        (report.getTaskManagement() != null && !"Completed".equals(report.getTaskManagement().getStatus())) ||
                                (report.getActivity() != null && !"Completed".equals(report.getActivity().getStatus()))
                )
                .collect(Collectors.toList());
        int pendingCount = reportService.countPendingReports();
        int gradedCount = reportService.countGradedReports();
        List<Integer> pendingReportIds = reportService.findPendingReportIds();

        model.addAttribute("reports", reports);
        model.addAttribute("pendingCount", pendingCount);
        model.addAttribute("gradedCount", gradedCount);
        model.addAttribute("pendingReportIds", pendingReportIds);
        model.addAttribute("pendingReports", pendingReports);
        return "report/hodReport";
    }

    // Project Manager views Supervisor Reports
    @GetMapping("/pm-report")
    public String viewPendingSupervisorReport(Model model){
        List<PendingSupervisorReportDTO> pendingReports = reportService.findPendingReportsBySupervisors();
        List<Report> reports = reportService.findAll().stream()
                .filter(report ->
                        (report.getTaskManagement() != null && !"Completed".equals(report.getTaskManagement().getStatus())) ||
                                (report.getActivity() != null && !"Completed".equals(report.getActivity().getStatus()))
                )
                .collect(Collectors.toList());
        int pendingCount = reportService.countPendingReports();
        int gradedCount = reportService.countGradedReports();
        List<Integer> pendingReportIds = reportService.findPendingReportIds();

        System.out.println("Pending Reports: " + pendingReports);


        model.addAttribute("reports", reports);
        model.addAttribute("pendingCount", pendingCount);
        model.addAttribute("gradedCount", gradedCount);
        model.addAttribute("pendingReportIds", pendingReportIds);
        model.addAttribute("pendingReports", pendingReports);
        return "report/pmReport";
    }

    // Supervisor views Task Leader Reports
    @GetMapping("/sp-report")
    public String viewPendingTaskLeaderReport(Model model, HttpSession session) {

        Integer userId = (Integer) session.getAttribute("userId");

        List<PendingReportDTO> pendingReports = reportService.findPendingReportsByTaskLeader();
        int pendingCount = reportService.countPendingReports();
        int gradedCount = reportService.countGradedReports();

        model.addAttribute("pendingReports", pendingReports);
        model.addAttribute("pendingCount", pendingCount);
        model.addAttribute("gradedCount", gradedCount);
        model.addAttribute("userId", userId);
        return "report/reports";
    }


    // Task Leader views Employees Reports
    @GetMapping("/em-report")
    public String viewPendingEmployeeReport(Model model, HttpSession session){

        Integer userId = (Integer) session.getAttribute("userId");
        List<Map<String,Object>> pendingReports = reportService.findPendingReportsByEmployees();
        List<Report> reports = reportService.findReportsByTaskLeaderUserId(userId).stream()
                .filter(report ->
                        (report.getTaskManagement() != null && !"Completed".equals(report.getTaskManagement().getStatus())) ||
                                (report.getActivity() != null && !"Completed".equals(report.getActivity().getStatus()))
                )
                .collect(Collectors.toList());
        int pendingCount = reportService.countPendingReports();
        int gradedCount = reportService.countGradedReports();
        List<Integer> pendingReportIds = reportService.findPendingReportIds();

        model.addAttribute("reports", reports);
        model.addAttribute("pendingCount", pendingCount);
        model.addAttribute("gradedCount", gradedCount);
        model.addAttribute("pendingReportIds", pendingReportIds);
        model.addAttribute("pendingReports", reports);
        return "report/taskLeaderReport";
    }

    @GetMapping("/download-report")
    public ResponseEntity<Resource> downloadReport(@RequestParam("reportId") int reportId) {
        Report report = reportService.findById(reportId);
        if (report == null || report.getReportFile() == null) {
            return ResponseEntity.notFound().build();
        }

        // Get the upload directory
        String uploadDir = "C:/Users/amine/Downloads/Jane/";  // Update this if necessary

        // Create the file path based on the report name
        Path filePath = Paths.get(uploadDir, report.getReportFile());

        // Check if the file exists
        if (!Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        }

        try {
            // Create a resource to serve the file
            Resource resource = new UrlResource(filePath.toUri());

            // Return the file as an attachment to download
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + report.getReportFile() + "\"")
                    .body(resource);
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping("/submitReports")
    @ResponseBody
    public ResponseEntity<?> submitReport(
            @RequestParam("reportFile") MultipartFile reportFile,
            @RequestParam("progressPercentage") Integer progressPercentage,
            @RequestParam("reportDescription") String reportDescription,
            @RequestParam("userId") Integer userId) {

        User user = usersService.findById(userId);
        Employee employee = employeeService.findByUser(user);


        // Fetch the latest report for the task by this employee
        Report latestReport = reportService.findLatestReportByEmployee(employee);

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
        report.setReportFile(filePath);
        report.setProgressPercentage(progressPercentage);
        report.setReportDescription(reportDescription);
        report.setSubmissionDate(new Date());

        reportService.save(report);

        return ResponseEntity.ok("Report submitted successfully.");
    }




    @GetMapping("/view-report")
    @ResponseBody
    public Report viewReport(@RequestParam("id") int reportId) {
        // Fetch the report details using the reportId
        return reportService.findById(reportId);
    }

    @GetMapping("/api/report-details/{reportId}")
    @ResponseBody
    public ResponseEntity<ReportDetailsDto> getReportDetails(@PathVariable int reportId) {
        Report report = reportService.findById(reportId);
        if (report == null) {
            return ResponseEntity.notFound().build();
        }



        ReportDetailsDto reportDetailsDto = new ReportDetailsDto();
        reportDetailsDto.setReportId(report.getId());
        reportDetailsDto.setReportDescription(report.getReportDescription());
        String fileUrl = "/reports/" + report.getReportFile();
        reportDetailsDto.setReportFile(fileUrl);
        reportDetailsDto.setSubmissionDate(report.getSubmissionDate());
        reportDetailsDto.setProgressPercentage(report.getProgressPercentage());


        // Assuming you need nested objects:
        TaskManagementDto taskManagementDto = new TaskManagementDto();
        taskManagementDto.setId(report.getTaskManagement().getId());
        taskManagementDto.setName(report.getTaskManagement().getName());
        taskManagementDto.setStatus(report.getTaskManagement().getStatus());
        taskManagementDto.setProjectName(report.getTaskManagement().getProject().getName());


        reportDetailsDto.setTaskManagement(taskManagementDto);

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(report.getEmployee().getId());
        employeeDto.setName(report.getEmployee().getName());

        reportDetailsDto.setEmployee(employeeDto);

        return ResponseEntity.ok(reportDetailsDto);
    }

    @GetMapping("/api/activity-details/{reportActivityId}")
    @ResponseBody
    public ResponseEntity<ReportDetailsDto> getActivityReportDetails(@PathVariable int reportActivityId) {
        Report report = reportService.findById(reportActivityId);
        if (report == null) {
            return ResponseEntity.notFound().build();
        }



        ReportDetailsDto reportDetailsDto = new ReportDetailsDto();
        reportDetailsDto.setReportId(report.getId());
        reportDetailsDto.setReportDescription(report.getReportDescription());
        String fileUrl = "/reports/" + report.getReportFile();
        reportDetailsDto.setReportFile(fileUrl);
        reportDetailsDto.setSubmissionDate(report.getSubmissionDate());
        reportDetailsDto.setProgressPercentage(report.getProgressPercentage());


        // Assuming you need nested objects:
        ActivityDetailDto activityDetailDto = new ActivityDetailDto();
        activityDetailDto.setActivityId(report.getActivity().getId());
        activityDetailDto.setActivityName(report.getActivity().getActivityName());
        activityDetailDto.setProjectName(report.getActivity().getProject().getName());


        reportDetailsDto.setActivityDetailDto(activityDetailDto);

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(report.getEmployee().getId());
        employeeDto.setName(report.getEmployee().getName());

        reportDetailsDto.setEmployee(employeeDto);

        return ResponseEntity.ok(reportDetailsDto);
    }

    @PostMapping("/grade-report")
    public RedirectView gradeReport(
            @RequestParam("reportId") int reportId,
            @RequestParam("employeeId") int employeeId,
            @RequestParam("grade") String grade,
            @RequestParam("comments") String comments,
            @RequestParam("gradingDate") String gradingDate) {

        try {
            // Parse gradingDate from String to Date
            Date gradingDateParsed = java.sql.Date.valueOf(gradingDate);

            // Call the service method to handle the grading logic
            boolean isGraded = gradingService.gradeReport(reportId, employeeId, grade, comments, gradingDateParsed);

            if (isGraded) {
                // Redirect to /reports-page if grading was successful
                return new RedirectView("/reports-page");
            } else {
                // Handle the failure case, you may choose to redirect to an error page or show an error message
                return new RedirectView("/reports-page?error=true");
            }
        } catch (Exception e) {
            // Log the exception and return a redirect to an error page
            e.printStackTrace();
            return new RedirectView("/reports-page?error=true");
        }
    }


    @GetMapping("/graded-reports-count")
    @ResponseBody
    public ResponseEntity<Integer> getGradedReportsCount() {
        int count = gradingService.countGradedReports();
        return ResponseEntity.ok(count);
    }

    @PostMapping("/complete-report")
    @ResponseBody
    public ResponseEntity<String> completeReport(@RequestBody Map<String, Integer> requestData) {
        int reportId = requestData.get("reportId");
        int employeeId = requestData.get("employeeId");

        Optional<Report> reportOpt = reportRepo.findById(reportId);
        if (reportOpt.isPresent()) {
            Report report = reportOpt.get();

            // Check if the report is associated with a TaskManagement or an Activity
            if (report.getTaskManagement() != null) {
                // If it's a task, update the task status to 'Completed'
                TaskManagement task = report.getTaskManagement();
                task.setStatus("Completed");
                task.setCompleted(true);
                task.setEndTime(LocalDateTime.now());
                taskManagementService.save(task);
                return ResponseEntity.ok("Task completed successfully");
            } else if (report.getActivity() != null) {
                // If it's an activity, complete the activity (adjust as necessary for your Activity class)
                Activity activity = report.getActivity();
                activity.setStatus("Completed"); // Assuming Activity has a setStatus method
                activityService.save(activity); // Use your service to save the updated activity
                return ResponseEntity.ok("Activity completed successfully");
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to complete the report");
    }




    @GetMapping("/api/progress-status/{reportId}")
    public ResponseEntity<Map<String, Boolean>> getProgressStatus(@PathVariable int reportId) {
        Report report = reportService.findById(reportId);
        boolean isComplete = (report.getProgressPercentage() == 100);

        Map<String, Boolean> response = new HashMap<>();
        response.put("isComplete", isComplete);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/my-reports")
    public String viewMyReports(Model model, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/view"; // or an appropriate error page
        }

        User user = usersService.findById(userId);
        Employee employee = employeeService.findByUser(user);

        // Retrieve all reports submitted by the logged-in employee
        List<Report> reports = reportService.findReportsByEmployeeId(employee.getId());

        // Log the reports for debugging
        System.out.println("Reports retrieved for employee ID " + employee.getId() + ": " + reports.size());

        // Add to model
        model.addAttribute("reports", reports);
        model.addAttribute("userId", userId);
        return "report/my-report"; // Return the Thymeleaf template for employee-exclusive reports
    }





}
