package com.ProjectPro.ProjectPro.controller;

import com.ProjectPro.ProjectPro.entity.*;
import com.ProjectPro.ProjectPro.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserController {

    private UsersService usersService;
    private RoleService roleService;
    private TaskManagementService taskService;
    private DirectorateService directorateService;
    private EmployeeService employeeService;
    private ReportService reportService;
    private ProjectService projectService;
    private TaskManagementService taskManagementService;
    private ActivityService activityService;

    @Autowired
    public UserController(UsersService usersService, RoleService roleService, TaskManagementService taskService, DirectorateService directorateService, EmployeeService employeeService, ReportService reportService, ProjectService projectService, TaskManagementService taskManagementService, ActivityService activityService) {
        this.usersService = usersService;
        this.roleService = roleService;
        this.taskService = taskService;
        this.directorateService = directorateService;
        this.employeeService = employeeService;
        this.reportService = reportService;
        this.projectService = projectService;
        this.taskManagementService = taskManagementService;
        this.activityService = activityService;
    }

    @GetMapping("/view")
    public String getPage() {
        return "users/loginPage";
    }

    @GetMapping("/viewSignUpPage")
    public String getSignUpPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.findAll());
        return "users/signUpPage";
    }

    @GetMapping("/accessDeniedPage")
    public String accessDenied() {
        return "accessDenied";
    }

    @PostMapping("/saveUser")
    public String saveUser(@RequestParam("roleId") int roleId,
                           @RequestParam("name") String name,
                           @RequestParam("department") String department,
                           @RequestParam("employeeType") String employmentType,
                           @RequestParam("email") String email,
                           @RequestParam("password") String password) {

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        User savedUser = usersService.save(user);
        usersService.assignRoleToUser(savedUser.getId(), roleId);

        Employee employee = new Employee();
        employee.setEmployeeType(employmentType);
        employee.setName(name);
        employee.setDepartment(department);
        employee.setUsers(savedUser);
        employeeService.save(employee);

        return "redirect:/view";
    }

    @PostMapping("/perform_login")
    public String performLogin(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               HttpSession session,
                               Model model) {
        User user = usersService.validateUser(username, password);

        if (user != null) {
            session.setAttribute("userId", user.getId());
            session.setAttribute("userRole", user.getRoles());
            return redirectToRoleBasedPage(user);
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "users/loginPage";
        }
    }

    private String redirectToRoleBasedPage(User user) {
        boolean isEmployee = user.getRoles().stream()
                .anyMatch(role -> role.getRoleName().equals("EMPLOYEE"));

        boolean isSupervisor = user.getRoles().stream()
                .anyMatch(role -> role.getRoleName().equals("SUPERVISOR"));

        boolean isHod = user.getRoles().stream()
                .anyMatch(role -> role.getRoleName().equals("HOD"));

        boolean isPm = user.getRoles().stream()
                .anyMatch(role -> role.getRoleName().equals("PROJECTMANAGER"));

        if (isEmployee) {
            return "redirect:/employee-home"; // Renamed path
        } else if (isSupervisor) {
            return "redirect:/supervisor-home"; // Renamed path
        } else if (isHod) {
            return "redirect:/hod-home"; // Renamed path
        } else if (isPm) {
            return "redirect:/project-manager-home"; // Renamed path
        } else {
            return "redirect:/accessDeniedPage";
        }
    }

    @GetMapping("/supervisor-home")
    public String viewSupervisorPage(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        User user = usersService.findById(userId);
        Employee employee = employeeService.findByUser(user);

        // Ensure user ID is still set in the session
        session.setAttribute("userId", userId);
        model.addAttribute("users", usersService.findAll());
        model.addAttribute("projects", projectService.findAll());
        return checkUserRole(session, "SUPERVISOR", "supervisor/supervisorPage");
    }
    @GetMapping("/hod-home")
    public String viewHodPage(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        User user = usersService.findById(userId);
        Employee employee = employeeService.findByUser(user);

        // Ensure user ID is still set in the session
        session.setAttribute("userId", userId);

        model.addAttribute("users", usersService.findAll());
        model.addAttribute("projects", projectService.findAll());
        return checkUserRole(session, "HOD", "hod/hodPage");
    }

    @GetMapping("/project-manager-home")
    public String viewProjectManagerPage(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        User user = usersService.findById(userId);
        Employee employee = employeeService.findByUser(user);

        // Ensure user ID is still set in the session
        session.setAttribute("userId", userId);

        model.addAttribute("users", usersService.findAll());
        model.addAttribute("projects", projectService.findAll());
        return checkUserRole(session, "PROJECTMANAGER", "projectManager/projectManagerPage");
    }

    @GetMapping("/employee-home")
    public String viewEmployeeDashboard(HttpSession session, Model model) {
        // Retrieve user ID from session
        Integer userId = (Integer) session.getAttribute("userId");

        // Redirect if user ID is not present in session
        if (userId == null) {
            return "redirect:/view";
        }

        // Retrieve user and employee details
        User user = usersService.findById(userId);
        Employee employee = employeeService.findByUser(user);

        // Ensure user ID is still set in the session
        session.setAttribute("userId", userId);

        // Retrieve and filter tasks for the logged-in user
        List<TaskManagement> sortedTasks = taskManagementService.getSortedTasksForLoggedInUser(user);
        List<Activity> sortedActivities = activityService.getSortedActivitiesForLoggedInUser(user);

        // Debug logging to verify the fetched activities
        System.out.println("Activities retrieved for employee: " + employee.getName());
        sortedActivities.forEach(activity -> System.out.println(activity.getActivityName()));


        // Filter tasks by their status
        List<TaskManagement> newTasks = sortedTasks.stream()
                .filter(task -> "New".equals(task.getStatus()))
                .collect(Collectors.toList());

        List<TaskManagement> inProgressTasks = sortedTasks.stream()
                .filter(task -> "In Progress".equals(task.getStatus()))
                .collect(Collectors.toList());

        List<TaskManagement> completedTasks = sortedTasks.stream()
                .filter(task -> "Completed".equals(task.getStatus()))
                .collect(Collectors.toList());

        // Filter activities by their status
        List<Activity> newActivities = sortedActivities.stream()
                .filter(activity -> "New".equals(activity.getStatus()))
                .collect(Collectors.toList());

        List<Activity> inProgressActivities = sortedActivities.stream()
                .filter(activity -> "In Progress".equals(activity.getStatus()))
                .collect(Collectors.toList());

        List<Activity> completedActivities = sortedActivities.stream()
                .filter(activity -> "Completed".equals(activity.getStatus()))
                .collect(Collectors.toList());

        // Log the number of activities in each category
        System.out.println("New Activities: " + newActivities.size());
        System.out.println("In Progress Activities: " + inProgressActivities.size());
        System.out.println("Completed Activities: " + completedActivities.size());

        // Find if the user is the task leader for any task
        TaskManagement taskAsLeader = sortedTasks.stream()
                .filter(task -> task.getTaskLeader() != null && task.getTaskLeader().getId() == employee.getId())
                .findFirst()
                .orElse(null);

        String taskNameAsLeader = taskAsLeader != null ? taskAsLeader.getName() : ""; // Get task name if available
        System.out.println("Task Name As Leader: " + taskNameAsLeader);

        // Pass the task leader ID and logged-in user ID to the view
        if (taskAsLeader != null) {
            model.addAttribute("taskLeaderId", taskAsLeader.getTaskLeader().getId());
        } else {
            model.addAttribute("taskLeaderId", -1);  // Use -1 or another identifier for non-leader
        }

        model.addAttribute("userId", userId);  // Pass the logged-in user ID

        // Add filtered tasks and activities to the model
        model.addAttribute("newTasks", newTasks);
        model.addAttribute("inTasks", inProgressTasks);
        model.addAttribute("completedTasks", completedTasks);
        model.addAttribute("newActivities", newActivities);
        model.addAttribute("inActivities", inProgressActivities);
        model.addAttribute("completedActivities", completedActivities);
        model.addAttribute("employees", employeeService.findAll());
        model.addAttribute("tasks", sortedTasks);
        model.addAttribute("employees", employeeService.findAll());
        model.addAttribute("employeeId", employee.getId());
        model.addAttribute("taskNameAsLeader", taskNameAsLeader);

        // Ensure proper role check
        return checkUserRole(session, "EMPLOYEE", "employee/empPage");
    }



    private String checkUserRole(HttpSession session, String requiredRole, String viewName) {
        Object rolesObj = session.getAttribute("userRole");
        if (rolesObj != null) {
            @SuppressWarnings("unchecked")
            List<Role> roles = (List<Role>) rolesObj; // Changed to List<Role>

            boolean hasRequiredRole = roles.stream()
                    .anyMatch(role -> role.getRoleName().equals(requiredRole)); // Compare role names

            if (hasRequiredRole) {
                return viewName;
            }
        }
        return "redirect:/accessDeniedPage";
    }


    @GetMapping("/viewAssignRolePage")
    public String viewAssignRolePage(Model model) {
        model.addAttribute("users", usersService.findAll());
        model.addAttribute("roles", roleService.findAll());
        return "users/assignRole";
    }

    @PostMapping("/assignRole")
    public String assignRolePage(@RequestParam("userId") int userId, @RequestParam("roleId") int roleId) {
        usersService.assignRoleToUser(userId, roleId);
        return "redirect:/viewAssignRolePage";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // Invalidate the session
        session.invalidate();
        // Redirect to the login or home page
        return "redirect:/view";
    }
}
