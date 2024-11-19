package com.ProjectPro.ProjectPro.Dto;

import com.ProjectPro.ProjectPro.entity.Employee;

import java.util.List;

public class ActivityDetailDto {
    private int activityId;
    private String activityName;
    private String activityNotes;
    private String status;
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

    public ActivityDetailDto() {
    }

    public ActivityDetailDto(int activityId, String activityName, String activityNotes, String status, int projectId, String projectName, String projectDescription, int objectiveId, String objectiveTitle, String objectiveDescription, int subObjectiveId, String subObjectiveTitle, String subObjectiveDescription, List<Employee> assignedEmployees) {
        this.activityId = activityId;
        this.activityName = activityName;
        this.activityNotes = activityNotes;
        this.status = status;
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

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityNotes() {
        return activityNotes;
    }

    public void setActivityNotes(String activityNotes) {
        this.activityNotes = activityNotes;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
