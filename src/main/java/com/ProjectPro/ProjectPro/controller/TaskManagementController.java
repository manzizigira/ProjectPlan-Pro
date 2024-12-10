package com.ProjectPro.ProjectPro.controller;

import com.ProjectPro.ProjectPro.entity.*;
import com.ProjectPro.ProjectPro.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@Controller
@RequestMapping("/task")
public class TaskManagementController {

    private TaskManagementService taskManagementService;

    private EmployeeService employeeService;

    private ActivityService activityService;

    private UsersService usersService;

    private RoleService roleService;

    private MessageService messageService;

    @Autowired
    public TaskManagementController(TaskManagementService taskManagementService, EmployeeService employeeService, ActivityService activityService, UsersService usersService, RoleService roleService, MessageService messageService) {
        this.taskManagementService = taskManagementService;
        this.employeeService = employeeService;
        this.activityService = activityService;
        this.usersService = usersService;
        this.roleService = roleService;
        this.messageService = messageService;
    }

    @GetMapping("/taskPage")
    public String taskPage(@RequestParam(value = "taskId", required = false) Integer taskId, Model model, HttpSession session){

        Integer userId = (Integer) session.getAttribute("userId");

        List<TaskManagement> taskManagementList = taskManagementService.findTaskManagementsByProjectManagerUserId(userId);

        String department = employeeService.findDepartmentByUserId(userId);
        List<Employee> supervisorList = employeeService.findSupervisorsByDepartment(department);

        model.addAttribute("tasks", taskManagementList);
        model.addAttribute("employees", supervisorList);

        model.addAttribute("employeeCount", 1);

        if (taskId != null) {
            model.addAttribute("task", taskManagementService.findById(taskId));
        }

        model.addAttribute("activities", activityService.findAll());

        return "task/tasksPage";
    }

    @GetMapping("/taskListPage")
    public String taskListPage(@RequestParam(value = "taskId", required = false) Integer taskId,Model model){
        List<Employee> employees = employeeService.findAll();
        model.addAttribute("tasks", taskManagementService.findAll());
        model.addAttribute("employees", employeeService.findAll());

        model.addAttribute("employeeCount", 1);

        if (taskId != null) {
            model.addAttribute("task", taskManagementService.findById(taskId));
        }

        model.addAttribute("activities", activityService.findAll());

        return "task/taskList";
    }

    @GetMapping("/taskSupervisorPage")
    public String taskSupervisorPage(@RequestParam(value = "taskId", required = false) Integer taskId,Model model, HttpSession session){

        Integer userId = (Integer) session.getAttribute("userId");

        String department = employeeService.findDepartmentByUserId(userId);
        List<Employee> employeeList = employeeService.findEmployeesByDepartment(department);

        List<Employee> employees = employeeService.findAll();
        model.addAttribute("tasks", taskManagementService.findAll());
        model.addAttribute("employees", employeeList);

        model.addAttribute("employeeCount", 1);

        if (taskId != null) {
            model.addAttribute("task", taskManagementService.findById(taskId));
        }

        model.addAttribute("activities", activityService.findAll());

        return "task/taskSupervisor";
    }

    @PostMapping("/taskPopulate")
    public String processTaskPage(
            @RequestParam(value = "taskId", required = false) Integer taskId,
            @RequestParam ("activityName") String activityName,
            @RequestParam ("notes") String notes,
            @RequestParam("employeeIds[]") List<Integer> employeeIds,
            @RequestParam(value = "taskLeaderId", required = false) Integer taskLeaderId,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        TaskManagement taskManagementId = taskManagementService.findById(taskId);

        if (taskLeaderId != null){
            Employee taskLeader = employeeService.findById(taskLeaderId);
            taskManagementId.setTaskLeader(taskLeader);
        }

        List<Employee> employees = employeeService.findAllById(employeeIds);
        taskManagementId.setEmployees(employees);

        Activity activity = new Activity();
        activity.setActivityName(activityName);
        activity.setNotes(notes);
        activity = activityService.save(activity);
        taskManagementId.setActivities(activity);
        activity.setTask(taskManagementId);

        taskManagementService.save(taskManagementId);

        if (taskLeaderId != null) {
            Employee taskLeader = employeeService.findById(taskLeaderId);

            // Assuming Employee is related to User (either extends or has a reference)
            User taskLeaderUser = taskLeader.getUser();  // Assuming this method exists to get User object

            // Retrieve the SUPERVISOR (current logged-in user)
            Integer userId = (Integer) session.getAttribute("userId");
            User supervisor = usersService.findById(userId);

            // Retrieve the Role for HOD and Project Manager
            Role supervisorRole = roleService.findByRoleName("SUPERVISOR");
            Role employeeRole = roleService.findByRoleName("EMPLOYEE");

            // Create the message between HOD and SUPERVISOR about the task leader assignment
            messageService.createSupervisorToTaskLeaderAssignmentMessage(supervisor, taskLeaderUser, taskManagementId, supervisorRole, employeeRole);

            // Create messages between Task Leader and all Employees
            for (Employee employee : employees) {
                User employeeUser = employee.getUser();  // Assuming this method exists
                messageService.createAssignmentMessageBetweenTaskLeaderAndEmployees(taskLeaderUser, employeeUser, taskManagementId, supervisorRole, employeeRole);
            }

        }

        // Redirect back to the task page
        return "redirect:/task/taskSupervisor";
    }



    @GetMapping("/employees")
    @ResponseBody // This indicates that the return value is written directly to the response body
    public List<Employee> getEmployees() {
        List<Employee> employees = employeeService.findAll();
        System.out.println("Fetched Employees: " + employees); // Debug log
        return employees;
    }

    @PostMapping("/update-task")
    public String updatePage(@ModelAttribute("taskId") TaskManagement taskManagement){
        taskManagementService.save(taskManagement);
        return "redirect:/task/taskPage";
    }


    @PostMapping("/delete")
    public String deletePage(@RequestParam("taskId") int id){
        taskManagementService.delete(id);
        return "redirect:/task/taskPage";
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> findByIdPage(@PathVariable int id){
        TaskManagement taskManagement = taskManagementService.findById(id);
        Map<String, Object> response = new HashMap<>();
        response.put("id", taskManagement.getId());
        response.put("name", taskManagement.getName());
        response.put("category", taskManagement.getCategory());
        response.put("startDate", taskManagement.getStartDate());
        response.put("endDate", taskManagement.getEndDate());
        response.put("notes", taskManagement.getNotes());
        response.put("status", taskManagement.getStatus());

        return ResponseEntity.ok(response);
    }


//    @PostMapping("/{taskId}/assign-activities")
//    public String assignActivitiesAndEmployees(@PathVariable("taskId") int taskId,
//                                               @RequestParam("activityIds") List<Integer> activityIds,
//                                               @RequestParam("employeeIds") List<Integer> employeeIds) {
//        // Call the service to assign activities and employees
//        taskManagementService.assignActivitiesAndEmployees(taskId, activityIds, employeeIds);
//
//        // Redirect back to the task page or some confirmation page
//        return "redirect:/task/taskPage";
//    }


}
