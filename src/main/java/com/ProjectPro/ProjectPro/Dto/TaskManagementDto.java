package com.ProjectPro.ProjectPro.Dto;

public class TaskManagementDto {
    private int id;
    private String name;
    private String status; // Assuming this is a status like 'Pending', 'In Progress', etc.
    private String projectName; // If you want to include the project name

    // Getters and Setters
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

}
