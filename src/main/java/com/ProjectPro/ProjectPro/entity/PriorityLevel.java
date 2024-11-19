package com.ProjectPro.ProjectPro.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "priority_level")
public class PriorityLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "estimated_cost_score")
    private double estimatedCost;

    @Column(name = "community_benefit_score")
    private double communityBenefitScore;

    @Column(name = "stakeholder_engagement_score")
    private double stakeholderEngagementScore;

    @Column(name = "priority_value")
    private String priorityValue;

    @OneToOne(mappedBy = "priorityLevel", fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Project project;

    @OneToOne(mappedBy = "priorityLevel",fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Activity activity;

    @OneToOne(mappedBy = "priorityLevel",fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private TaskManagement taskManagement;

    public PriorityLevel() {
    }

    public PriorityLevel(double estimatedCost, double communityBenefitScore, double stakeholderEngagementScore, String priorityValue) {
        this.estimatedCost = estimatedCost;
        this.communityBenefitScore = communityBenefitScore;
        this.stakeholderEngagementScore = stakeholderEngagementScore;
        this.priorityValue = priorityValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(double estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    public double getCommunityBenefitScore() {
        return communityBenefitScore;
    }

    public void setCommunityBenefitScore(double communityBenefitScore) {
        this.communityBenefitScore = communityBenefitScore;
    }

    public double getStakeholderEngagementScore() {
        return stakeholderEngagementScore;
    }

    public void setStakeholderEngagementScore(double stakeholderEngagementScore) {
        this.stakeholderEngagementScore = stakeholderEngagementScore;
    }

    public String getPriorityValue() {
        return priorityValue;
    }

    public void setPriorityValue(String priorityValue) {
        this.priorityValue = priorityValue;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public TaskManagement getTaskManagement() {
        return taskManagement;
    }

    public void setTaskManagement(TaskManagement taskManagement) {
        this.taskManagement = taskManagement;
    }
}
