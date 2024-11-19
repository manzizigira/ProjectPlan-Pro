package com.ProjectPro.ProjectPro.service;

import com.ProjectPro.ProjectPro.entity.Activity;
import com.ProjectPro.ProjectPro.entity.PriorityLevel;
import com.ProjectPro.ProjectPro.entity.Project;
import com.ProjectPro.ProjectPro.entity.TaskManagement;

import java.util.List;

public interface PriorityLevelService {

    PriorityLevel save(PriorityLevel priorityLevel);

    void delete(int theId);

    PriorityLevel findById(int theId);

    List<PriorityLevel> findAll();

    public String getPrediction(double estimatedCost, double communityBenefitScore, double stakeholderEngagementScore, Project project);

    public String getTaskPrediction(int taskId, double estimatedCost, double communityBenefitScore, double stakeholderEngagementScore, TaskManagement taskManagement);

    public String getActivityPrediction(int activityId, double estimatedCost, double communityBenefitScore, double stakeholderEngagementScore, Activity activity);

    public List<PriorityLevel> getAllPriorityLevelsSorted();

    public double parsePriorityValue(String priorityValue);
}
