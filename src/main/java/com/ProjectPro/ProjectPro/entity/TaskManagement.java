package com.ProjectPro.ProjectPro.entity;

import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "task_management")
public class TaskManagement {

    // create fields
    // annotate them with column names
    // create a relationship between task and project
    // create a relationship between task and employee
    // create constructors
    // create getters and setters
    // and the toString method

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @NotNull
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "category")
    private String category;

    @Column(name = "start_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @Column(name = "end_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    @Column(name = "notes")
    private String notes;

    @Column(name = "status")
    private String status;

    @Column(name = "start_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime endTime;

    @Column(name = "is_started", nullable = true)
    private Boolean isStarted;

    @Column(name = "is_completed", nullable = true)
    private Boolean isCompleted;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Report> reports;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "task_employee",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id")
    )
    private List<Employee> employees;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "priority_level_id")
    private PriorityLevel priorityLevel;

    @OneToOne(mappedBy = "task",fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Activity activities;

    @ManyToOne
    @JoinColumn(name = "task_leader_id", nullable = true) // Direct foreign key column for the leader
    private Employee taskLeader;

    @OneToOne
    @JoinColumn(name = "supervisor_id", nullable = true)
    private Employee supervisor;

    public TaskManagement() {
        this.employees = new ArrayList<>();
        this.reports = new ArrayList<>();
    }

    public TaskManagement(String name, String category, Date startDate, Date endDate, String notes, String status) {
        this.name = name;
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
        this.notes = notes;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public List<Report> getReports() {
        return reports;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }

    public PriorityLevel getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(PriorityLevel priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public Activity getActivities() {
        return activities;
    }

    public void setActivities(Activity activities) {
        this.activities = activities;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Boolean getStarted() {
        return isStarted != null ? isStarted : false;
    }

    public void setStarted(Boolean started) {
        isStarted = started;
    }

    public Boolean getCompleted() {
        return isCompleted;
    }

    public void setCompleted(Boolean completed) {
        isCompleted = completed;
    }

    public Employee getTaskLeader() {
        return taskLeader;
    }

    public void setTaskLeader(Employee taskLeader) {
        this.taskLeader = taskLeader;
    }

    public Employee getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Employee supervisor) {
        this.supervisor = supervisor;
    }

    // add convenience method
    public void addEmployee(Employee employee){
        if (employees == null){
            employees = new ArrayList<>();
        }
        employees.add(employee);
    }

    public void addReports(Report report){
        if (reports.isEmpty()){
            reports = new ArrayList<>();
        }
        reports.add(report);
    }



    @Override
    public String toString() {
        return "TaskManagement{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", notes='" + notes + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
