package com.ProjectPro.ProjectPro.Dto;

import java.util.Date;

public class PendingTaskLeaderReport {

    private Integer reportId;
    private String reportDescription;
    private String taskName;
    private String employeeName;
    private Date submissionDate;

    public PendingTaskLeaderReport(Integer reportId, String reportDescription, String taskName, String employeeName, Date submissionDate) {
        this.reportId = reportId;
        this.reportDescription = reportDescription;
        this.taskName = taskName;
        this.employeeName = employeeName;
        this.submissionDate = submissionDate;
    }

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
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

    public String getReportDescription() {
        return reportDescription;
    }

    public void setReportDescription(String reportDescription) {
        this.reportDescription = reportDescription;
    }
}
