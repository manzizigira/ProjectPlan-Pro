package com.ProjectPro.ProjectPro.Dto;

import java.util.Date;

public class PendingSupervisorReportDTO {

    private Integer reportId;
    private String reportTitle;
    private String supervisorName;
    private Date submissionDate;
    private String status;

    public PendingSupervisorReportDTO(Integer reportId, String reportTitle, String supervisorName, Date submissionDate, String status) {
        this.reportId = reportId;
        this.reportTitle = reportTitle;
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

}
