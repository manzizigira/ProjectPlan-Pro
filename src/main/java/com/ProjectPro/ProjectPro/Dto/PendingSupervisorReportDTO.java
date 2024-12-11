package com.ProjectPro.ProjectPro.Dto;

import java.util.Date;

public class PendingSupervisorReportDTO {

    private Integer reportId;
    private String reportTitle;
    private Integer taskId;
    private String taskName;
    private String supervisorName;
    private Date submissionDate;
    private String status;

    public PendingSupervisorReportDTO(Integer reportId, String reportTitle, Integer taskId, String taskName, String supervisorName, Date submissionDate, String status) {
        this.reportId = reportId;
        this.reportTitle = reportTitle;
        this.taskId = taskId;
        this.taskName = taskName;
        this.supervisorName = supervisorName;
        this.submissionDate = submissionDate;
        this.status = status;
    }

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public String getReportTitle() {
        return reportTitle;
    }

    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }


    public String getSupervisorName() {
        return supervisorName;
    }

    public void setSupervisorName(String supervisorName) {
        this.supervisorName = supervisorName;
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

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}
