package com.ProjectPro.ProjectPro.controller;


import com.ProjectPro.ProjectPro.entity.*;
import com.ProjectPro.ProjectPro.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/project")
public class ProjectController {

    private ProjectService projectService;

    private TaskManagementService taskManagementService;

    private ActivityService activityService;

    private EmployeeService employeeService;

    private MessageService messageService;

    private RoleService roleService;

    private UsersService usersService;

    @Autowired
    public ProjectController(ProjectService projectService, TaskManagementService taskManagementService, ActivityService activityService, EmployeeService employeeService, MessageService messageService, RoleService roleService, UsersService usersService) {
        this.projectService = projectService;
        this.taskManagementService = taskManagementService;
        this.activityService = activityService;
        this.employeeService = employeeService;
        this.messageService = messageService;
        this.roleService = roleService;
        this.usersService = usersService;
    }

    @GetMapping("/projectPage")
    public String projectPage(Model model, HttpSession session){
        Integer userId = (Integer) session.getAttribute("userId");
        List<Project> projectList = projectService.findProjectsByUserId(userId);

        String department = employeeService.findDepartmentByUserId(userId);
        List<Employee> supervisorList = employeeService.findSupervisorsByDepartment(department);

        String departments = employeeService.findDepartmentByUserId(userId);
        List<Employee> employeeList = employeeService.findEmployeesByDepartment(departments);

        model.addAttribute("projects", projectList);
        model.addAttribute("employees", supervisorList);
        model.addAttribute("employs", employeeList);

        return "project/projectPage";
    }
    @GetMapping("/projectListPage")
    public String projectList(Model model, HttpSession session){
        Integer userId = (Integer) session.getAttribute("userId");
        List<Project> projectList = projectService.findProjectsByDirectorateUserId(userId);
        model.addAttribute("projectLists", projectList);

        return "project/projectList";
    }

    @PostMapping("/add-project")
    public String saveProject(@ModelAttribute("projects")Project project){
        projectService.save(project);
        return "redirect:/project/projectPage";
    }

    @PostMapping("/update-project")
    public String updateProject(
            @RequestParam("projectId") int projectId,
            @RequestParam("name") String name,
            @RequestParam("category") String category,
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam("description") String description) {

        // Fetch the project by ID (assume projectService exists)
        Project project = projectService.findById(projectId);

        // Update project details
        project.setName(name);
        project.setCategory(category);
        project.setStartDate(startDate);
        project.setEndDate(endDate);
        project.setDescription(description);

        // Save the updated project (assume projectService exists)
        projectService.save(project);

        return "redirect:/project/projectPage"; // Redirect to projects page or any other suitable location
    }

    @PostMapping("/delete-project")
    public String deleteProject(@RequestParam("projectId") int id){
        projectService.delete(id);
        return "redirect:/project/projectPage";
    }

    @GetMapping("/projects/{id}")
    public ResponseEntity<Map<String, Object>> findByIdPage(@PathVariable int id){
        Project project =  projectService.findById(id);
        Map<String, Object> response = new HashMap<>();
        response.put("id", project.getId());
        response.put("name", project.getName());
        response.put("category", project.getCategory());
        response.put("startDate", project.getStartDate());
        response.put("endDate", project.getEndDate());
        response.put("description", project.getDescription());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add-task")
    public String addTaskPage(@RequestParam("projectId") int project,
                              @RequestParam("name") String name,
                              @RequestParam("category") String category,
                              @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                              @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                              @RequestParam("notes") String notes,
                              @RequestParam(value = "supervisorId", required = false) Integer supervisorId,
                              RedirectAttributes redirectAttributes,
                              HttpSession session) {

        Project project1 = projectService.findById(project);

        TaskManagement taskManagement = new TaskManagement();
        taskManagement.setName(name);
        taskManagement.setCategory(category);
        taskManagement.setStartDate(java.sql.Date.valueOf(startDate));
        taskManagement.setEndDate(java.sql.Date.valueOf(endDate));
        taskManagement.setNotes(notes);
        taskManagement.setStatus("New");
        taskManagement.setProject(project1);

        if(supervisorId != null){
            Employee supervisor = employeeService.findById(supervisorId);
            taskManagement.setSupervisor(supervisor);
        }

        taskManagementService.save(taskManagement);

        if (supervisorId != null) {
            Employee supervisor = employeeService.findById(supervisorId);

            // Assuming Employee is related to User (either extends or has a reference)
            User supervisorUser = supervisor.getUser();  // Assuming this method exists to get User object

            // Retrieve the HOD (current logged-in user)
            Integer userId = (Integer) session.getAttribute("userId");
            User hod = usersService.findById(userId);

            // Retrieve the Role for HOD and Project Manager
            Role hodRole = roleService.findByRoleName("PROJECTMANAGER");
            Role projectManagerRole = roleService.findByRoleName("PROJECTMANAGER");

            // Create the message between PROJECTMANAGER and SUPERVISOR about the task assignment
            messageService.createTaskToSupervisorAssignmentMessage(hod, supervisorUser, taskManagement, hodRole, projectManagerRole);

        }

        return "redirect:/project/projectPage";
    }

    @PostMapping("/add-activity")
    public String addActivityPage(@RequestParam("projectId") int project,
                                  @RequestParam("activityName") String name,
                                  @RequestParam("notes") String notes,
                                  @RequestParam("employeeId") Integer employeeId,
                                  HttpSession session){

        Project project1 = projectService.findById(project);
        Employee employees = employeeService.findById(employeeId);

        Activity activity = new Activity();
        activity.setActivityName(name);
        activity.setNotes(notes);
        activity.setStatus("New");
        activity.setProject(project1);
        activity.setEmployee(employees);

        activityService.save(activity);

        if (employeeId != null) {
            Employee employee = employeeService.findById(employeeId);

            // Assuming Employee is related to User (either extends or has a reference)
            User employeeUser = employee.getUser();  // Assuming this method exists to get User object

            // Retrieve the HOD (current logged-in user)
            Integer userId = (Integer) session.getAttribute("userId");
            User hod = usersService.findById(userId);

            // Retrieve the Role for HOD and Project Manager
            Role hodRole = roleService.findByRoleName("PROJECTMANAGER");
            Role projectManagerRole = roleService.findByRoleName("EMPLOYEE");

            // Create the message between PROJECTMANAGER and EMPLOYEE about the activity assignment
            messageService.createActivityAssignmentMessage(hod, employeeUser, activity, hodRole, projectManagerRole);

        }

        return "redirect:/project/projectPage";
    }
}
