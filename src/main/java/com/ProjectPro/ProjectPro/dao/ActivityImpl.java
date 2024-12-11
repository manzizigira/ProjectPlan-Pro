package com.ProjectPro.ProjectPro.dao;

import com.ProjectPro.ProjectPro.entity.Activity;
import com.ProjectPro.ProjectPro.entity.Employee;
import com.ProjectPro.ProjectPro.entity.User;
import com.ProjectPro.ProjectPro.repository.ActivityRepo;
import com.ProjectPro.ProjectPro.service.ActivityService;
import com.ProjectPro.ProjectPro.service.EmployeeService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ActivityImpl implements ActivityService {

    private ActivityRepo activityRepo;

    private EmployeeService employeeService;

    @Autowired
    public ActivityImpl(ActivityRepo activityRepo, EmployeeService employeeService) {
        this.activityRepo = activityRepo;
        this.employeeService = employeeService;
    }

    @Override
    public Activity save(Activity activity) {
        return activityRepo.save(activity);
    }

    @Override
    public void saveActivity(int activityId) {
        Activity activity = activityRepo.findById(activityId)
                .orElseThrow(() -> new EntityNotFoundException("Activity not found with id " + activityId));

        activity.setStartTime(LocalDateTime.now());
        activity.setStarted(true);
        // Update task status
        activity.setStatus("In Progress");
        activityRepo.save(activity);
    }

    @Override
    public Activity findById(int theId) {
        Optional<Activity> results = activityRepo.findById(theId);
        Activity activity = null;
        if (results.isPresent()){
            activity = results.get();
        }else {
            throw new RuntimeException("ID not Found!");
        }
        return activity;
    }

    @Override
    public void delete(int theId) {
        activityRepo.deleteById(theId);
    }

    @Override
    public List<Activity> findAll() {
        return activityRepo.findAll();
    }


    @Override
    public Activity findByEmployee(Employee employee) {
        return null;
    }

    @Override
    public List<Activity> getSortedActivitiesForLoggedInUser(User user) {
        // Retrieve the employee based on the user
        Employee employee = employeeService.findByUser(user);
        if (employee == null) {
            return Collections.emptyList(); // Return an empty list if no employee is found
        }

        // Fetch the activity (assuming one-to-one relationship)
        Activity activity = activityRepo.findByEmployee(employee);
        if (activity == null) {
            return Collections.emptyList(); // Return an empty list if no activity is found
        }

        // Create a list with the single activity and sort
        return Stream.of(activity)
                .sorted(Comparator.comparing(a -> a.getPriorityLevel().getPriorityValue(), Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }



    @Override
    public ResponseEntity<Activity> escalateActivity(int activityId, int newEmployeeId) {
        try {
            // Retrieve the activity by activityId
            Activity activity = this.findById(activityId);
            if (activity == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // Activity not found
            }

            // Find the new employee by newEmployeeId
            Employee newEmployee = employeeService.findById(newEmployeeId);
            if (newEmployee == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // New employee not found
            }

            // Check if the new employee is already assigned to this activity
            if (newEmployee.equals(activity.getEmployee())) {
                return new ResponseEntity<>(activity, HttpStatus.OK);  // No changes needed
            }

            // Update the relationship
            // Remove the activity from the current employee (if bidirectional)
            Employee currentEmployee = activity.getEmployee();
            if (currentEmployee != null) {
                currentEmployee.setActivities(null);
                employeeService.save(currentEmployee); // Update the current employee if necessary
            }

            // Assign the new employee to the activity
            activity.setEmployee(newEmployee);

            // Assign the activity to the new employee (if bidirectional)
            newEmployee.setActivities(activity);
            employeeService.save(newEmployee); // Update the new employee if necessary

            // Save the updated activity
            Activity updatedActivity = activityRepo.save(activity);

            return new ResponseEntity<>(updatedActivity, HttpStatus.OK);  // Return the updated activity
        } catch (Exception e) {
            // Log the error and return a server error status
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @Override
    public List<Activity> findCompletedActivities() {
        return activityRepo.findByStatus("Completed");
    }

    @Override
    public List<Activity> getActivitiesForEmployee(Employee employee) {
        return activityRepo.findActivitiesByEmployee(employee);
    }

    @Override
    public List<Activity> findActivitiesByProjectManagerUserId(int userId) {
        return activityRepo.findActivitiesByProjectManagerUserId(userId);
    }

    @Override
    public List<Activity> findActivitiesByHeadOfDepartmentUserId(int userId) {
        return activityRepo.findActivitiesByHeadOfDepartmentUserId(userId);
    }
}
