package com.ProjectPro.ProjectPro.entity;

import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.Date;

@Entity
@Table(name = "grading")
public class Grading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @NotNull
    private int id;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "report_id")
    private Report report;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "grade")
    private String grade;

    @Column(name = "grading_date")
    private Date gradingDate;

    @Column(name = "comments")
    private String comments;

    public Grading(){

    }

    public Grading(String grade, Date gradingDate, String comments) {
        this.grade = grade;
        this.gradingDate = gradingDate;
        this.comments = comments;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Date getGradingDate() {
        return gradingDate;
    }

    public void setGradingDate(Date gradingDate) {
        this.gradingDate = gradingDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Grading{" +
                "id=" + id +
                ", grade='" + grade + '\'' +
                ", gradingDate=" + gradingDate +
                ", comments='" + comments + '\'' +
                '}';
    }
}
