package com.ProjectPro.ProjectPro.Dto;

public class EmployeeDto {
    private int id;
    private String name;


    public EmployeeDto(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public EmployeeDto() {
    }

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
}
