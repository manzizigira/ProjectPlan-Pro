package com.ProjectPro.ProjectPro.controller;

import com.ProjectPro.ProjectPro.entity.MessageModel;
import com.ProjectPro.ProjectPro.entity.Role;
import com.ProjectPro.ProjectPro.entity.User;
import com.ProjectPro.ProjectPro.service.MessageService;
import com.ProjectPro.ProjectPro.service.RoleService;
import com.ProjectPro.ProjectPro.service.UsersService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;
    private final UsersService usersService;
    private final RoleService roleService;

    @Autowired
    public MessageController(MessageService messageService, UsersService usersService, RoleService roleService) {
        this.messageService = messageService;
        this.usersService = usersService;
        this.roleService = roleService;
    }

    @GetMapping("/view")
    public String viewMessages(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        User currentUser = usersService.findById(userId);
        String role = currentUser.getRoles().get(0).getRoleName(); // Assuming roles list is not empty

        List<MessageModel> messages = null;
        switch (role) {
            case "HOD":
                messages = messageService.getMessagesForHOD(currentUser);
                break;
            case "PROJECTMANAGER":
                messages = messageService.getMessagesForProjectManager(currentUser);
                break;
            case "SUPERVISOR":
                messages = messageService.getMessagesForSupervisor(currentUser);
                break;
            case "EMPLOYEE":
                messages = messageService.getMessagesForEmployee(currentUser);
                break;
        }

        model.addAttribute("messages", messages);
        return "messagePage"; // This is the page that displays the messages for each user
    }

    @PostMapping("/post")
    public String postMessage(
            @RequestParam("userId") Integer userId,
            @RequestParam(value = "replyId", required = false) Long replyId,
            @RequestParam("message") String messageContent,
            HttpSession session) {
        User sender = usersService.findById(userId);
        User receiver = null;

        // If it's a reply, fetch the original message to identify the receiver
        if (replyId != null) {
            MessageModel originalMessage = messageService.getMessageById(replyId);
            receiver = originalMessage != null ? originalMessage.getSender() : null;
        }

        if (receiver != null) {
            // Create reply
            messageService.createMessage(sender, receiver, messageContent, null, null, null);
        } else {
            // Create new message (adjust logic as needed)
            messageService.createMessage(sender, null, messageContent, null, null, null);
        }

        return "redirect:/messages/view";
    }


}
