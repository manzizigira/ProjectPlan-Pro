package com.ProjectPro.ProjectPro.controller;

import com.ProjectPro.ProjectPro.Dto.UserChat;
import com.ProjectPro.ProjectPro.entity.*;
import com.ProjectPro.ProjectPro.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;
    private final UsersService usersService;
    private final RoleService roleService;
    private final EmployeeService employeeService;
    private final ProjectService projectService;

    @Autowired
    public MessageController(MessageService messageService, UsersService usersService, RoleService roleService, EmployeeService employeeService, ProjectService projectService) {
        this.messageService = messageService;
        this.usersService = usersService;
        this.roleService = roleService;
        this.employeeService = employeeService;
        this.projectService = projectService;
    }

    @GetMapping("/view")
    public String viewMessages(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        User currentUser = usersService.findById(userId);
        String roleName = currentUser.getRoles().get(0).getRoleName();

        // Fetch the Role entity based on role name
        Role role = roleService.findByRoleName(roleName);
        if (role == null) {
            model.addAttribute("error", "Role not found.");
            return "message/messagePage";
        }

        Employee employee = employeeService.findByUser(currentUser);
        Project project = employee.getProjects().isEmpty() ? null : employee.getProjects().get(0);
        Integer projectId = project != null ? project.getId() : null;
        List<MessageModel> messages = null;

        if (projectId == null) {
            model.addAttribute("error", "No project associated with this employee.");
            return "message/messagePage";
        }

        // Modify the logic to use Role entity
        if (role.getRoleName().equals("HOD")) {
            messages = messageService.findByProjectIdAndReceiverRole(projectId, role);
        } else if (role.getRoleName().equals("PROJECTMANAGER")) {
            messages = messageService.findByProjectIdAndReceiverRole(projectId, role);
        }

        // Populate the side panel with dynamic chat names
        // In your controller
        List<Map<String, Object>> chatNames = new ArrayList<>();
        if ("HOD".equals(roleName)) {
            chatNames.add(Map.of("id", project.getProjectManager().getId(), "name", project.getProjectManager().getName()));
        } else if ("PROJECTMANAGER".equals(roleName)) {
            chatNames.add(Map.of("id", project.getDirectorate().getHeadOfDirectorate().getId(), "name", project.getDirectorate().getHeadOfDirectorate().getName()));
        }

        model.addAttribute("chatNames", chatNames);
        model.addAttribute("currentUser", userId);
        model.addAttribute("messages", messages);

        return "message/messagePage";
    }


    @PostMapping("/post")
    public String postMessage(
            @RequestParam("userId") Integer userId,
            @RequestParam(value = "replyId", required = false) Integer replyId,
            @RequestParam("message") String messageContent,
            HttpSession session) {

        User sender = usersService.findById(userId);
        User receiver = null;

        // Handle reply logic
        if (replyId != null) {
            MessageModel originalMessage = messageService.getMessageById(replyId);
            receiver = originalMessage != null ? originalMessage.getSender() : null;
        }

        if (receiver != null) {
            messageService.createMessage(sender, receiver, messageContent, null, null, null);  // Reply
        } else {
            messageService.createMessage(sender, null, messageContent, null, null, null);  // New message
        }

        return "redirect:/messages/view";  // Or use AJAX to refresh the messages without page reload
    }


    @GetMapping("/chat/{senderId}/{receiverId}")
    @ResponseBody
    public List<MessageModel> getMessagesBySenderAndReceiver(
            @PathVariable("senderId") int senderId,
            @PathVariable("receiverId") int receiverId, Model model, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        model.addAttribute("currentUser", userId);
        return messageService.findMessagesByChatId(senderId, receiverId);
    }





}
