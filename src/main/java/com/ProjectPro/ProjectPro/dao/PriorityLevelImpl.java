package com.ProjectPro.ProjectPro.dao;

import com.ProjectPro.ProjectPro.entity.Activity;
import com.ProjectPro.ProjectPro.entity.PriorityLevel;
import com.ProjectPro.ProjectPro.entity.Project;
import com.ProjectPro.ProjectPro.entity.TaskManagement;
import com.ProjectPro.ProjectPro.repository.ActivityRepo;
import com.ProjectPro.ProjectPro.repository.PriorityLevelRepo;
import com.ProjectPro.ProjectPro.repository.ProjectRepo;
import com.ProjectPro.ProjectPro.repository.TaskManagementRepo;
import com.ProjectPro.ProjectPro.service.PriorityLevelService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PriorityLevelImpl implements PriorityLevelService {

    private PriorityLevelRepo priorityLevelRepo;

    private RestTemplate restTemplate;

    private ProjectRepo projectRepo;

    private TaskManagementRepo taskManagementRepo;

    private ActivityRepo activityRepo;

    @Autowired
    public PriorityLevelImpl(PriorityLevelRepo priorityLevelRepo, RestTemplate restTemplate, ProjectRepo projectRepo, TaskManagementRepo taskManagementRepo, ActivityRepo activityRepo) {
        this.priorityLevelRepo = priorityLevelRepo;
        this.restTemplate = restTemplate;
        this.projectRepo = projectRepo;
        this.taskManagementRepo = taskManagementRepo;
        this.activityRepo = activityRepo;
    }

    @Override
    public PriorityLevel save(PriorityLevel priorityLevel) {
        return priorityLevelRepo.save(priorityLevel);
    }

    @Override
    public void delete(int theId) {
        priorityLevelRepo.deleteById(theId);
    }

    @Override
    public PriorityLevel findById(int theId) {
        Optional<PriorityLevel> result = priorityLevelRepo.findById(theId);

        PriorityLevel priorityLevel = null;

        if (result.isPresent()){
            priorityLevel = result.get();
        }
        return priorityLevel;
    }

    @Override
    public List<PriorityLevel> findAll() {
        return priorityLevelRepo.findAll();
    }
    @Override
    public String getPrediction(double estimatedCost, double communityBenefitScore, double stakeholderEngagementScore, Project project) {
        String url = "http://localhost:8000/predicts";

        // Set up the request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("estimated_cost", estimatedCost);
        requestBody.put("community_benefit_score", communityBenefitScore);
        requestBody.put("stakeholder_engagement_score", stakeholderEngagementScore);

        // Set up headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create the HTTP entity
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        // Send the POST request to the Flask API
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        // Retrieve the response body as a String
        String responseBody = response.getBody();

        // Parse the JSON response to extract the value
        ObjectMapper objectMapper = new ObjectMapper();
        String priorityValue = null;
        try {
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            priorityValue = jsonNode.get("predicted_priority_value").asText();
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions as needed
        }

        // Create a PriorityLevel object
        PriorityLevel priorityLevel = new PriorityLevel();
        priorityLevel.setEstimatedCost(estimatedCost);
        priorityLevel.setCommunityBenefitScore(communityBenefitScore);
        priorityLevel.setStakeholderEngagementScore(stakeholderEngagementScore);
        priorityLevel.setPriorityValue(priorityValue);

        // Set the PriorityLevel to the Project
        project.setPriorityLevel(priorityLevel);

        // Save the PriorityLevel object to the database
        priorityLevelRepo.save(priorityLevel);

        // Update the Project with the new PriorityLevel
        projectRepo.save(project);

        // Return the extracted priority value
        return priorityValue;
    }

    @Override
    public String getTaskPrediction(int taskId, double estimatedCost, double communityBenefitScore, double stakeholderEngagementScore, TaskManagement taskManagement) {
        String url = "http://localhost:5000/predict";

        // Set up the request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("estimated_cost_price", estimatedCost);
        requestBody.put("community_benefit_score", communityBenefitScore);
        requestBody.put("stakeholder_engagement_score", stakeholderEngagementScore);

        // Set up headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create the HTTP entity
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        // Send the POST request to the Flask API
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        // Retrieve the response body as a String
        String responseBody = response.getBody();

        // Parse the JSON response to extract the value
        ObjectMapper objectMapper = new ObjectMapper();
        String priorityValue = null;
        try {
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            priorityValue = jsonNode.get("predicted_priority_value").asText();
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions as needed
        }

        // Create a PriorityLevel object
        PriorityLevel priorityLevel = new PriorityLevel();
        priorityLevel.setEstimatedCost(estimatedCost);
        priorityLevel.setCommunityBenefitScore(communityBenefitScore);
        priorityLevel.setStakeholderEngagementScore(stakeholderEngagementScore);
        priorityLevel.setPriorityValue(priorityValue);

        // Set the PriorityLevel to the Project
        taskManagement.setPriorityLevel(priorityLevel);

        // Save the PriorityLevel object to the database
        priorityLevelRepo.save(priorityLevel);

        // Find the associated task
        TaskManagement task = taskManagementRepo.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));


        // Update the Project with the new PriorityLevel
        taskManagementRepo.save(task);

        // Return the extracted priority value
        return priorityValue;
    }

    @Override
    public String getActivityPrediction(int activityId, double estimatedCost, double communityBenefitScore, double stakeholderEngagementScore, Activity activity) {
        String url = "http://localhost:5000/predict";

        // Set up the request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("Estimated Cost Price", estimatedCost);
        requestBody.put("Community Benefit Score", communityBenefitScore);
        requestBody.put("Stakeholder Engagement Score", stakeholderEngagementScore);

        // Set up headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create the HTTP entity
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        // Send the POST request to the Flask API
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        // Retrieve the response body as a String
        String responseBody = response.getBody();

        // Parse the JSON response to extract the priority value
        ObjectMapper objectMapper = new ObjectMapper();
        String priorityValue = null;
        try {
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            priorityValue = jsonNode.get("Predicted Priority Value").asText();
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions as needed
        }

        // Create a PriorityLevel object
        PriorityLevel priorityLevel = new PriorityLevel();
        priorityLevel.setEstimatedCost(estimatedCost);
        priorityLevel.setCommunityBenefitScore(communityBenefitScore);
        priorityLevel.setStakeholderEngagementScore(stakeholderEngagementScore);
        priorityLevel.setPriorityValue(priorityValue);

        // Set the PriorityLevel to the Activity
        activity.setPriorityLevel(priorityLevel);

        // Save the PriorityLevel object to the database
        priorityLevelRepo.save(priorityLevel);

        // Find the associated activity
        Activity existingActivity = activityRepo.findById(activityId)
                .orElseThrow(() -> new RuntimeException("Activity not found"));

        // Update the Activity with the new PriorityLevel
        activityRepo.save(existingActivity);

        // Return the extracted priority value
        return priorityValue;
    }

    @Override
    public List<PriorityLevel> getAllPriorityLevelsSorted() {
        List<PriorityLevel> priorityLevels = priorityLevelRepo.findAll();
        // Sort by priorityValue in descending order
        return priorityLevels.stream()
                .sorted((pl1, pl2) -> {
                    double value1 = parsePriorityValue(pl1.getPriorityValue());
                    double value2 = parsePriorityValue(pl2.getPriorityValue());
                    return Double.compare(value2, value1); // Sort in descending order
                })
                .collect(Collectors.toList());
    }

    @Override
    public double parsePriorityValue(String priorityValue) {
        try {
            return Double.parseDouble(priorityValue);
        } catch (NumberFormatException e) {
            // Handle the case where parsing fails; return a default value
            return Double.NEGATIVE_INFINITY; // or some other value to push it to the bottom
        }
    }


}
