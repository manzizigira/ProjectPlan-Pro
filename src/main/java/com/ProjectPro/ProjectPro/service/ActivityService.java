package com.ProjectPro.ProjectPro.service;



import com.ProjectPro.ProjectPro.entity.Activity;
import com.ProjectPro.ProjectPro.entity.Employee;
import com.ProjectPro.ProjectPro.entity.User;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ActivityService {

    Activity save(Activity activity);

    public void saveActivity(int activityId);

    Activity findById(int theId);

    void delete(int theId);

    List<Activity> findAll();


    Activity findByEmployee(Employee employee);

    public List<Activity> getSortedActivitiesForLoggedInUser(User user);

    public ResponseEntity<Activity> escalateActivity(int activityId, int employeeId);

    public List<Activity> findCompletedActivities();

    public List<Activity> getActivitiesForEmployee(Employee employee);

    List<Activity> findActivitiesByProjectManagerUserId(@Param("userId") int userId);

    List<Activity> findActivitiesByHeadOfDepartmentUserId(@Param("userId") int userId);
}
