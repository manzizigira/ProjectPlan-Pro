package com.ProjectPro.ProjectPro.Dto;

import com.ProjectPro.ProjectPro.entity.Employee;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class PendingReportDTO {

    private Integer reportId;
    private String reportTitle;
    private String taskName;
    private String taskLeader;
    private Date submissionDate;
    private String status;
    private Integer employee;

    public PendingReportDTO(Integer reportId, String reportTitle, String taskName, String taskLeader, Date submissionDate, String status, Integer employee) {
        this.reportId = reportId;
        this.reportTitle = reportTitle;
        this.taskName = taskName;
        this.taskLeader = taskLeader;
        this.submissionDate = submissionDate;
        this.status = status;
        this.employee = employee;
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

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskLeader() {
        return taskLeader;
    }

    public void setTaskLeader(String taskLeader) {
        this.taskLeader = taskLeader;
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

    public Integer getEmployee() {
        return employee;
    }

    public void setEmployee(Integer employee) {
        this.employee = employee;
    }
}
