package com.ProjectPro.ProjectPro.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.Date;

@Entity
@Table(name = "report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @NotNull
    private int id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "employee_id")
    @JsonIgnore
    private Employee employee;

    @OneToOne(mappedBy = "report")
    private Grading grading;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "task_id")
    @JsonIgnore
    private TaskManagement taskManagement;

    @Column(name = "report_file")
    private String reportFile;

    @Column(name = "progress_percentage")
    private int progressPercentage;

    @Column(name = "report_description")
    private String reportDescription;

    @Column(name = "submission_date")
    private Date submissionDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "activity_id")
    @JsonIgnore
    private Activity activity;

    public Report(){

    }

    public Report(String reportFile, int progressPercentage, String reportDescription, Date submissionDate) {
        this.reportFile = reportFile;
        this.progressPercentage = progressPercentage;
        this.reportDescription = reportDescription;
        this.submissionDate = submissionDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public TaskManagement getTaskManagement() {
        return taskManagement;
    }

    public void setTaskManagement(TaskManagement taskManagement) {
        this.taskManagement = taskManagement;
    }

    public String getReportFile() {
        return reportFile;
    }

    public void setReportFile(String reportFile) {
        this.reportFile = reportFile;
    }

    public String getReportDescription() {
        return reportDescription;
    }

    public void setReportDescription(String reportDescription) {
        this.reportDescription = reportDescription;
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

    public Grading getGrading() {
        return grading;
    }

    public void setGrading(Grading grading) {
        this.grading = grading;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", reportFile='" + reportFile + '\'' +
                ", reportDescription='" + reportDescription + '\'' +
                ", submissionDate=" + submissionDate + '\'' +
                ", progressPercentage=" + progressPercentage +
                '}';
    }
}
