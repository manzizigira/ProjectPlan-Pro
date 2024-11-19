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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
    public void assignActivityToEmployee(int activityId, int employeeId) {
// Check if the employee is already assigned to this activity
        boolean isAssigned = activityRepo.existsByActivityIdAndEmployeeId(activityId, employeeId);

        if (!isAssigned) {
            // Proceed with assigning the activity to the employee
            Activity activity = this.findById(activityId);
            Employee employee = employeeService.findById(employeeId);

            activity.getEmployees().add(employee);
            activityRepo.save(activity);
        } else {
            // Optionally handle the case when the activity is already assigned
            System.out.println("This activity is already assigned to the employee.");
        }
    }


    @Override
    public Activity findByEmployee(Employee employee) {
        return null;
    }

    @Override
    public List<Activity> getSortedActivitiesForLoggedInUser(User user) {
        // Retrieve employee based on user
        Employee employee = employeeService.findByUser(user);

        // Fetch activities assigned to the employee
        List<Activity> activities = activityRepo.findAll();

        // Filter activities by employee and sort by priority level
        return activities.stream()
                .filter(activity -> activity.getEmployees().contains(employee)) // Assuming one-to-many relationship
                .sorted(Comparator.comparing(activity -> activity.getPriorityLevel().getPriorityValue()))
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

            // Find the current employees assigned to the activity
            Set<Employee> currentEmployees = activity.getEmployees(); // Assuming this is a Set<Employee>

            // If the new employee is already assigned, no action is needed
            if (currentEmployees.contains(newEmployee)) {
                return new ResponseEntity<>(activity, HttpStatus.OK);  // Return the existing activity
            }

            // Now assign the new employee to the activity
            activity.getEmployees().add(newEmployee); // Add the new employee to the activity's employee list

            // Add the activity to the new employee's list of activities (if bidirectional)
            newEmployee.getActivities().add(activity);
            // Optionally, save the new employee to update their activities (if necessary)
            employeeService.save(newEmployee);

            // Save the updated activity with the new employee assigned
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
        return activityRepo.findActivitiesByEmployees(employee);
    }
}
