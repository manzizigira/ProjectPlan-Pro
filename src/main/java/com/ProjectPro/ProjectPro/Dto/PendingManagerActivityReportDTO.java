package com.ProjectPro.ProjectPro.Dto;

import java.util.Date;

public class PendingManagerActivityReportDTO {

    private Integer reportId;
    private String reportDescription;
    private Integer activityId;
    private String activityName;
    private String projectName;
    private Integer employeeId;
    private String employeeName;
    private Date submissionDate;
    private String status;

    public PendingManagerActivityReportDTO(Integer reportId, String reportDescription, Integer activityId, String activityName, String projectName, Integer employeeId, String employeeName, Date submissionDate, String status) {
        this.reportId = reportId;
        this.reportDescription = reportDescription;
        this.activityId = activityId;
        this.activityName = activityName;
        this.projectName = projectName;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.submissionDate = submissionDate;
        this.status = status;
    }

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public String getReportDescription() {
        return reportDescription;
    }

    public void setReportDescription(String reportDescription) {
        this.reportDescription = reportDescription;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }
}
