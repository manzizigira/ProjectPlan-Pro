package com.ProjectPro.ProjectPro.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "employee")
public class Employee {

    // create fields
    // annotate them with column names
    // relationship with user login and employee
    // create a relationship between employee and task
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

    @Column(name = "contract_years")
    private String contractYears;

    @Column(name = "department")
    private String department;

    @Column(name = "employee_type")
    private String employeeType;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JsonIgnore
    private List<Report> reports;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "task_employee",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id")
    )
    @JsonIgnore
    private List<TaskManagement> taskManagements;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "activity_employee", // Join table name
            joinColumns = @JoinColumn(name = "employee_id"), // Column from Activity
            inverseJoinColumns = @JoinColumn(name = "activity_id") // Column from Employee
    )
    @JsonIgnore
    private Set<Activity> activities = new HashSet<>();

    @OneToMany(mappedBy = "taskLeader", fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JsonIgnore
    private List<TaskManagement> taskManagement;

    @OneToOne(mappedBy = "supervisor")
    private TaskManagement management;

    @OneToMany(mappedBy = "projectManager")
    private List<Project> projects;

    public Employee() {
        reports = new ArrayList<>();
        activities = new HashSet<>();
        taskManagements = new ArrayList<>();
    }

    public Employee(String name, String contractYears, String department, String employeeType) {
        this.name = name;
        this.contractYears = contractYears;
        this.department = department;
        this.employeeType = employeeType;
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

    public String getContractYears() {
        return contractYears;
    }

    public void setContractYears(String contractYears) {
        this.contractYears = contractYears;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(String employeeType) {
        this.employeeType = employeeType;
    }


    public User getUsers() {
        return user;
    }

    public void setUsers(User user) {
        this.user = user;
    }

    public List<TaskManagement> getTaskManagements() {
        return taskManagements;
    }

    public void setTaskManagements(List<TaskManagement> taskManagements) {
        this.taskManagements = taskManagements;
    }

    public List<Report> getReports() {
        return reports;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Activity> getActivities() {
        return activities;
    }

    public void setActivities(Set<Activity> activities) {
        this.activities = activities;
    }

    public List<TaskManagement> getTaskManagement() {
        return taskManagement;
    }

    public void setTaskManagement(List<TaskManagement> taskManagement) {
        this.taskManagement = taskManagement;
    }

    public TaskManagement getManagement() {
        return management;
    }

    public void setManagement(TaskManagement management) {
        this.management = management;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    // add convenience method
    public void addTask(TaskManagement taskManagement){
        if(taskManagements == null){
            taskManagements = new ArrayList<>();
        }
        taskManagements.add(taskManagement);
    }

    public void addReports(Report report){
        if (reports.isEmpty()){
            reports = new ArrayList<>();
        }
        reports.add(report);
    }

    public void addActivities(Activity activity) {
        this.activities.add(activity);
        activity.getEmployees().add(this);
    }


    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", contractYears='" + contractYears + '\'' +
                ", department='" + department + '\'' +
                ", employeeType='" + employeeType + '\'' +
                '}';
    }
}
