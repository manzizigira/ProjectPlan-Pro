package com.ProjectPro.ProjectPro.Dto;

public class GradingDto {
    private int id;
    private String score; // Assuming score is a string (e.g., 'A', 'B', 'C', etc.)
    private String comments;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
