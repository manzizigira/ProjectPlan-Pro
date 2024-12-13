package com.ProjectPro.ProjectPro.Dto;

import com.ProjectPro.ProjectPro.entity.Employee;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL) // Include non-null fields in JSON
@JsonSerialize
public class TaskDetailsDto {

    private int taskId;
    private String taskName;
    private String taskLeader;
    private String category;
    private Date startDate;
    private Date endDate;

    private Boolean isCompleted;
    private String status;

    private String activityName;
    private String activityNotes;

    // Project details
    private int projectId;
    private String projectName;
    private String projectDescription;

    // Directorate details
    private int objectiveId;
    private String objectiveTitle;
    private String objectiveDescription;

    // ImplementingAgency details (optional, might be null if the project doesn't belong to a sub-objective)
    private int subObjectiveId;
    private String subObjectiveTitle;
    private String subObjectiveDescription;

    // List of employees assigned to this task
    private List<Employee> assignedEmployees;

    // Constructor, Getters, and Setters

    public TaskDetailsDto() {
    }

    // Add constructors for easy initialization


    public TaskDetailsDto(int taskId, String taskName, String category, Date startDate, Date endDate, Boolean isCompleted, String status, String activityName, int projectId, String projectName, String projectDescription, int objectiveId, String objectiveTitle, String objectiveDescription, int subObjectiveId, String subObjectiveTitle, String subObjectiveDescription, List<Employee> assignedEmployees) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isCompleted = isCompleted;
        this.status = status;
        this.activityName = activityName;
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.objectiveId = objectiveId;
        this.objectiveTitle = objectiveTitle;
        this.objectiveDescription = objectiveDescription;
        this.subObjectiveId = subObjectiveId;
        this.subObjectiveTitle = subObjectiveTitle;
        this.subObjectiveDescription = subObjectiveDescription;
        this.assignedEmployees = assignedEmployees;
    }

    // Getters and Setters
    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public int getObjectiveId() {
        return objectiveId;
    }

    public void setObjectiveId(int objectiveId) {
        this.objectiveId = objectiveId;
    }

    public String getObjectiveTitle() {
        return objectiveTitle;
    }

    public void setObjectiveTitle(String objectiveTitle) {
        this.objectiveTitle = objectiveTitle;
    }

    public String getObjectiveDescription() {
        return objectiveDescription;
    }

    public void setObjectiveDescription(String objectiveDescription) {
        this.objectiveDescription = objectiveDescription;
    }

    public int getSubObjectiveId() {
        return subObjectiveId;
    }

    public void setSubObjectiveId(int subObjectiveId) {
        this.subObjectiveId = subObjectiveId;
    }

    public String getSubObjectiveTitle() {
        return subObjectiveTitle;
    }

    public void setSubObjectiveTitle(String subObjectiveTitle) {
        this.subObjectiveTitle = subObjectiveTitle;
    }

    public String getSubObjectiveDescription() {
        return subObjectiveDescription;
    }

    public void setSubObjectiveDescription(String subObjectiveDescription) {
        this.subObjectiveDescription = subObjectiveDescription;
    }

    public List<Employee> getAssignedEmployees() {
        return assignedEmployees;
    }

    public void setAssignedEmployees(List<Employee> assignedEmployees) {
        this.assignedEmployees = assignedEmployees;
    }

    public Boolean getCompleted() {
        return isCompleted;
    }

    public void setCompleted(Boolean completed) {
        isCompleted = completed;
    }

    public String getTaskLeader() {
        return taskLeader;
    }

    public void setTaskLeader(String taskLeader) {
        this.taskLeader = taskLeader;
    }

    public String getActivityNotes() {
        return activityNotes;
    }

    public void setActivityNotes(String activityNotes) {
        this.activityNotes = activityNotes;
    }
}