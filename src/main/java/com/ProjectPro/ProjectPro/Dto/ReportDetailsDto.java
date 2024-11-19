package com.ProjectPro.ProjectPro.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonSerialize
public class ReportDetailsDto {

    private int reportId;
    private String reportDescription;
    private String reportFile;
    private Date submissionDate;
    private int progressPercentage;
    private TaskManagementDto taskManagement;
    private ActivityDetailDto activityDetailDto;
    private EmployeeDto employee;
    private int employeeDto;
    private String status;
    private GradingDto grading;

    private boolean isActivity;

    private int projectId;
    private String projectName;

    public ReportDetailsDto() {
    }

    public ReportDetailsDto(int reportId, int employeeDto, boolean isActivity) {
        this.reportId = reportId;
        this.employeeDto = employeeDto;
        this.isActivity = isActivity;
    }

    // Getters and Setters
    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public String getReportDescription() {
        return reportDescription;
    }

    public void setReportDescription(String reportDescription) {
        this.reportDescription = reportDescription;
    }

    public String getReportFile() {
        return reportFile;
    }

    public void setReportFile(String reportFile) {
        this.reportFile = reportFile;
    }

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    public int getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(int progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    public TaskManagementDto getTaskManagement() {
        return taskManagement;
    }

    public void setTaskManagement(TaskManagementDto taskManagement) {
        this.taskManagement = taskManagement;
    }

    public EmployeeDto getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeDto employee) {
        this.employee = employee;
    }

    public GradingDto getGrading() {
        return grading;
    }

    public void setGrading(GradingDto grading) {
        this.grading = grading;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public ActivityDetailDto getActivityDetailDto() {
        return activityDetailDto;
    }

    public void setActivityDetailDto(ActivityDetailDto activityDetailDto) {
        this.activityDetailDto = activityDetailDto;

    }

    public boolean isActivity() {
        return isActivity;
    }

    public void setActivity(boolean activity) {
        isActivity = activity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getEmployeeDto() {
        return employeeDto;
    }

    public void setEmployeeDto(int employeeDto) {
        this.employeeDto = employeeDto;
    }
}