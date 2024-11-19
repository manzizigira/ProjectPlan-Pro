package com.ProjectPro.ProjectPro.controller;


import com.ProjectPro.ProjectPro.Dto.ActivityDetailDto;
import com.ProjectPro.ProjectPro.Dto.EmployeeDto;
import com.ProjectPro.ProjectPro.Dto.ReportDetailsDto;
import com.ProjectPro.ProjectPro.Dto.TaskManagementDto;
import com.ProjectPro.ProjectPro.entity.Activity;
import com.ProjectPro.ProjectPro.entity.Employee;
import com.ProjectPro.ProjectPro.entity.Report;
import com.ProjectPro.ProjectPro.entity.TaskManagement;
import com.ProjectPro.ProjectPro.service.ActivityService;
import com.ProjectPro.ProjectPro.service.EmployeeService;
import com.ProjectPro.ProjectPro.service.ReportService;
import com.ProjectPro.ProjectPro.service.TaskManagementService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.*;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.xhtmlrenderer.pdf.ITextRenderer;


@Controller
@RequestMapping("/history")
public class HistoryController {

    private TaskManagementService taskService;
    private ActivityService activityService;
    private EmployeeService employeeService;
    private ReportService reportService;

    @Autowired
    public HistoryController(TaskManagementService taskService, ActivityService activityService, EmployeeService employeeService, ReportService reportService) {
        this.taskService = taskService;
        this.activityService = activityService;
        this.employeeService = employeeService;
        this.reportService = reportService;
    }

    @GetMapping
    public String getHistoryPage(Model model) {
        // Retrieve completed tasks and activities
        List<TaskManagement> completedTasks = taskService.findCompletedTasks();
        List<Activity> completedActivities = activityService.findCompletedActivities();

        // Add completed tasks and activities to the model
        model.addAttribute("completedTasks", completedTasks);
        model.addAttribute("completedActivities", completedActivities);
        model.addAttribute("completedActivitiesCount", completedActivities.size());

        // Return the view name
        return "history/historyPage"; // This refers to history.html
    }

    @GetMapping("/api/employees")
    public ResponseEntity<List<EmployeeDto>> getEmployees() {
        List<Employee> employees = employeeService.findAll();
        List<EmployeeDto> employeeDTOs = employees.stream()
                .map(emp -> new EmployeeDto(emp.getId(), emp.getName()))
                .collect(Collectors.toList());

        return new ResponseEntity<>(employeeDTOs, HttpStatus.OK);
    }


    @GetMapping("/api/reports/{employeeId}")
    public ResponseEntity<List<ReportDetailsDto>> getEmployeeReports(@PathVariable int employeeId) {
        List<Report> reports = reportService.findReportsByEmployeeId(employeeId);

        // Map Report entities to ReportDetailsDto
        List<ReportDetailsDto> reportDtos = reports.stream()
                .map(report -> {
                    ReportDetailsDto dto = new ReportDetailsDto();
                    dto.setReportId(report.getId());
                    dto.setReportDescription(report.getReportDescription());
                    dto.setReportFile(report.getReportFile());
                    dto.setSubmissionDate(report.getSubmissionDate());
                    dto.setProgressPercentage(report.getProgressPercentage());

                    // If related to a Task, use its status and project details
                    if (report.getTaskManagement() != null) {
                        TaskManagementDto taskManagementDto = new TaskManagementDto();
                        taskManagementDto.setId(report.getTaskManagement().getId());
                        taskManagementDto.setName(report.getTaskManagement().getName());
                        dto.setTaskManagement(taskManagementDto);

                        // Set status from TaskManagement
                        dto.setStatus(report.getTaskManagement().getStatus());

                        // Set project details from TaskManagement's project
                        if (report.getTaskManagement().getProject() != null) {
                            dto.setProjectId(report.getTaskManagement().getProject().getId());
                            dto.setProjectName(report.getTaskManagement().getProject().getName());
                        }
                    }

                    // If related to an Activity, use its status and project details
                    if (report.getActivity() != null) {
                        ActivityDetailDto activityDetailDto = new ActivityDetailDto();
                        activityDetailDto.setActivityName(report.getActivity().getActivityName());
                        dto.setActivityDetailDto(activityDetailDto);

                        // Set status from Activity
                        dto.setStatus(report.getActivity().getStatus());

                        // Set project details from Activity's project
                        if (report.getActivity().getProject() != null) {
                            dto.setProjectId(report.getActivity().getProject().getId());
                            dto.setProjectName(report.getActivity().getProject().getName());
                        }
                    }

                    return dto;
                })
                .collect(Collectors.toList());

        return new ResponseEntity<>(reportDtos, HttpStatus.OK);
    }





    @GetMapping("/api/reports/{employeeId}/download")
    public void downloadReport(@PathVariable int employeeId, HttpServletResponse response)
            throws IOException, com.lowagie.text.DocumentException {

        // Fetch employee and their reports
        Employee employee = employeeService.findById(employeeId);
        List<Report> reports = reportService.findReportsByEmployeeId(employeeId);

        // Generate HTML content dynamically
        String htmlContent = generateHtmlForReport(employee, reports);

        // Set content type and headers for PDF download
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=employee_report_" + employeeId + ".pdf");

        // Use Flying Saucer to generate PDF
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmlContent);
        renderer.layout();

        // Write the generated PDF to the response output stream
        OutputStream outputStream = response.getOutputStream();
        renderer.createPDF(outputStream);
        outputStream.close();
    }





    private String generateHtmlForReport(Employee employee, List<Report> reports) throws IOException {
        StringBuilder htmlBuilder = new StringBuilder();

        // Get the Base64 encoded image string from the classpath
        String base64Logo = encodeImageToBase64FromResource("static/images/Coat_of_arms_of_Rwanda.png");

        // HTML and CSS styling
        htmlBuilder.append("<!DOCTYPE html>");
        htmlBuilder.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
        htmlBuilder.append("<head>");
        htmlBuilder.append("<style>");
        htmlBuilder.append("body { font-family: Arial, sans-serif; margin: 0; padding: 0; }");
        htmlBuilder.append(".header { text-align: center; background-color: #f0f0f0; padding: 20px; }");
        htmlBuilder.append(".logo { width: 100px; height: auto; display: block; margin: 0 auto; }");
        htmlBuilder.append(".content { margin: 20px; }");
        htmlBuilder.append(".table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
        htmlBuilder.append(".table th { background-color: #00bfff; color: white; border: 1px solid black; padding: 8px; text-align: left; }");
        htmlBuilder.append(".table td { border: 1px solid black; padding: 8px; }");
        htmlBuilder.append(".footer { text-align: center; font-size: 12px; margin-top: 50px; }");
        htmlBuilder.append("h1 { color: black; }");  // Ministry title in green
        htmlBuilder.append("</style>");
        htmlBuilder.append("</head>");
        htmlBuilder.append("<body>");

        // Ministry of Infrastructure header with Base64 logo
        htmlBuilder.append("<div class='header'>");
        htmlBuilder.append("<img class='logo' src='data:image/png;base64,").append(base64Logo).append("' alt='Logo' />");
        htmlBuilder.append("<h1>Ministry of Infrastructure</h1>");
        htmlBuilder.append("<hr />");
        htmlBuilder.append("</div>");

        // Employee information
        htmlBuilder.append("<div class='content'>");
        htmlBuilder.append("<h2>Employee Report Summary</h2>");
        htmlBuilder.append("<p><strong>Name:</strong> ").append(employee.getName()).append("</p>");
        htmlBuilder.append("<p><strong>Job Title:</strong> ").append(employee.getJobTitle()).append("</p>");
        htmlBuilder.append("<p><strong>Department:</strong> ").append(employee.getDepartment()).append("</p>");

        // Table for report data
        htmlBuilder.append("<table class='table'>");
        htmlBuilder.append("<tr>");
        htmlBuilder.append("<th>Date of Service</th>");
        htmlBuilder.append("<th>Report ID</th>");
        htmlBuilder.append("<th>Description</th>");
        htmlBuilder.append("<th>Progress</th>");
        htmlBuilder.append("<th>Submission Date</th>");
        htmlBuilder.append("</tr>");

        // Dynamic report data rows
        for (Report report : reports) {
            htmlBuilder.append("<tr>");
            htmlBuilder.append("<td>").append(report.getSubmissionDate()).append("</td>");
            htmlBuilder.append("<td>").append(report.getId()).append("</td>");
            htmlBuilder.append("<td>").append(report.getReportDescription()).append("</td>");
            htmlBuilder.append("<td>").append(report.getProgressPercentage()).append("%</td>");
            htmlBuilder.append("<td>").append(report.getSubmissionDate()).append("</td>");
            htmlBuilder.append("</tr>");
        }

        htmlBuilder.append("</table>");
        htmlBuilder.append("<br />");
        htmlBuilder.append("Print Date: " + LocalDate.now());
        htmlBuilder.append("</div>");

        // Footer information
        htmlBuilder.append("<div class='footer'>");
        htmlBuilder.append("For further assistance, contact: Ministry of Infrastructure");
        htmlBuilder.append("</div>");

        htmlBuilder.append("</body>");
        htmlBuilder.append("</html>");

        return htmlBuilder.toString();  // Return the complete HTML
    }


    private String encodeImageToBase64FromResource(String resourcePath) throws IOException {
        ClassPathResource imgFile = new ClassPathResource(resourcePath);
        byte[] bytes = StreamUtils.copyToByteArray(imgFile.getInputStream());
        return Base64.getEncoder().encodeToString(bytes);
    }



}
