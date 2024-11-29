package com.ProjectPro.ProjectPro.dao;

import com.ProjectPro.ProjectPro.entity.Employee;
import com.ProjectPro.ProjectPro.entity.TaskManagement;
import com.ProjectPro.ProjectPro.entity.User;
import com.ProjectPro.ProjectPro.repository.ActivityRepo;
import com.ProjectPro.ProjectPro.repository.EmployeeRepo;
import com.ProjectPro.ProjectPro.repository.TaskManagementRepo;
import com.ProjectPro.ProjectPro.service.EmployeeService;
import com.ProjectPro.ProjectPro.service.TaskManagementService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskManagementImpl implements TaskManagementService {

    private TaskManagementRepo taskManagementRepo;

    private EmployeeService employeeService;

    private ActivityRepo activityRepo;

    private EmployeeRepo employeeRepo;

    @Autowired
    public TaskManagementImpl(TaskManagementRepo taskManagementRepo, EmployeeService employeeService, ActivityRepo activityRepo, EmployeeRepo employeeRepo) {
        this.taskManagementRepo = taskManagementRepo;
        this.employeeService = employeeService;
        this.activityRepo = activityRepo;
        this.employeeRepo = employeeRepo;
    }

    @Override
    public TaskManagement save(TaskManagement taskManagement) {
        return taskManagementRepo.save(taskManagement);
    }

    @Override
    public TaskManagement findById(int theId) {
        Optional<TaskManagement> result = taskManagementRepo.findById(theId);

        TaskManagement taskManagement = null;

        if (result.isPresent()){
            taskManagement = result.get();
        }else{
            throw new RuntimeException("Id Not Found!");
        }
        return taskManagement;
    }

    @Override
    public void delete(int theId) {
        taskManagementRepo.deleteById(theId);
    }

    @Override
    public List<TaskManagement> findAll() {
        return taskManagementRepo.findAll();
    }

    @Override
    public List<TaskManagement> findTaskManagementIdByEmployeesId(int theId) {
        return taskManagementRepo.findTaskManagementIdByEmployeesId(theId);
    }

    @Override
    public List<TaskManagement> findTaskManagementsByEmployeeId(int theId) {
        return taskManagementRepo.findTaskManagementsByEmployeesId(theId);
    }

    @Override
    public TaskManagement findTaskManagementAndEmployeeByTaskManagementId(int theId) {
        return taskManagementRepo.findTaskManagementAndEmployeeByTaskManagementId(theId);
    }

    @Override
    public List<TaskManagement> findTaskManagementsByProjectId(int theId) {
        return taskManagementRepo.findTaskManagementsByProjectId(theId);
    }

    @Override
    public TaskManagement assignTaskToEmployees(int taskId, int employeeId) {
        TaskManagement taskManagement = this.findById(taskId);
        Employee employees = employeeService.findById(employeeId);

        if (!taskManagement.getEmployees().contains(employees)){
            taskManagement.addEmployee(employees);
        }

        return taskManagementRepo.save(taskManagement);
    }

    @Override
    public ResponseEntity<TaskManagement> escalateTask(int taskId, int employeeId) {
        try {
            TaskManagement taskManagement = this.findById(taskId);
            if (taskManagement == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // Task not found
            }

            Employee newEmployee = employeeService.findById(employeeId);
            if (newEmployee == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // New employee not found
            }

            // Find the existing employee who currently has the task
            Employee currentEmployee = taskManagement.getEmployees().stream().findFirst().orElse(null);
            if (currentEmployee != null) {
                taskManagement.getEmployees().remove(currentEmployee);
            }

            // Add the new employee to the task
            taskManagement.getEmployees().add(newEmployee);

            // Save the updated task
            TaskManagement updatedTask = taskManagementRepo.save(taskManagement);

            return new ResponseEntity<>(updatedTask, HttpStatus.OK);  // Return the updated task
        } catch (Exception e) {
            // Log the error and return a server error status
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public void startTask(int taskId) {
        TaskManagement task = taskManagementRepo.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id " + taskId));

        task.setStartTime(LocalDateTime.now());
        task.setStarted(true);
        // Update task status
        task.setStatus("In Progress");
        taskManagementRepo.save(task);
    }

    @Override
    public void endTask(int taskId) {
        TaskManagement task = taskManagementRepo.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
        task.setEndTime(LocalDateTime.now());
        task.setCompleted(true);
        taskManagementRepo.save(task);
    }

    @Override
    public boolean updateTaskStatus(int taskId, String status) {
        Optional<TaskManagement> optionalTask = taskManagementRepo.findById(taskId);
        if (optionalTask.isPresent()) {
            TaskManagement task = optionalTask.get();
            task.setStatus(status);
            taskManagementRepo.save(task);
            return true;
        }
        return false;
    }

    @Override
    public List<TaskManagement> getSortedTasksForLoggedInUser(User user) {
        // Retrieve employee based on user
        Employee employee = employeeService.findByUser(user);

        // Fetch tasks assigned to the employee
        List<TaskManagement> tasks = taskManagementRepo.findAll();

        // Filter tasks by assigned employee and sort by priority level
        return tasks.stream()
                .filter(task -> task.getEmployees().contains(employee)) // Assuming many-to-many relationship
                .sorted(Comparator.comparing(task -> task.getPriorityLevel().getPriorityValue()))
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskManagement> findCompletedTasks() {
        return taskManagementRepo.findByStatus("Completed");
    }



    @Override
    public List<TaskManagement> findTaskManagementsByProjectManagerUserId(int userId) {
        return taskManagementRepo.findTaskManagementsByProjectManagerUserId(userId);
    }
}
