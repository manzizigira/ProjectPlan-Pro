package com.ProjectPro.ProjectPro.Dto;

import java.util.Date;

public class PendingManagerReportDTO {

    private Integer reportId;
    private String reportDescription;
    private String projectName;
    private String projectManagerName;
    private Date submissionDate;
    private String status;

    public PendingManagerReportDTO(Integer reportId, String reportDescription, String projectName, String projectManagerName, Date submissionDate, String status) {
        this.reportId = reportId;
        this.reportDescription = reportDescription;
        this.projectName = projectName;
        this.projectManagerName = projectManagerName;
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

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectManagerName() {
        return projectManagerName;
    }

    public void setProjectManagerName(String projectManagerName) {
        this.projectManagerName = projectManagerName;
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
}
