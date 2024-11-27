package com.ProjectPro.ProjectPro.service;

import com.ProjectPro.ProjectPro.entity.TaskManagement;
import com.ProjectPro.ProjectPro.entity.User;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TaskManagementService {

    TaskManagement save(TaskManagement taskManagement);

    TaskManagement findById(int theId);

    void delete(int theId);

    List<TaskManagement> findAll();

    List<TaskManagement> findTaskManagementIdByEmployeesId(int theId);

    List<TaskManagement> findTaskManagementsByEmployeeId(int theId);

    TaskManagement findTaskManagementAndEmployeeByTaskManagementId(int theId);

    List<TaskManagement> findTaskManagementsByProjectId(int theId);

    TaskManagement assignTaskToEmployees(int taskId, int employeeId);

    public ResponseEntity<TaskManagement> escalateTask(int taskId, int employeeId);


    public void startTask(int taskId);
    public void endTask(int taskId);

    public boolean updateTaskStatus(int taskId, String status);

//    public void assignActivitiesAndEmployees(Integer taskId, List<Integer> activityIds, List<Integer> employeeIds);

    public List<TaskManagement> getSortedTasksForLoggedInUser(User user);

    public List<TaskManagement> findCompletedTasks();

    List<TaskManagement> findTaskManagementsByUserId(int userId);

}
