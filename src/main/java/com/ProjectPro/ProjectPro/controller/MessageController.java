package com.ProjectPro.ProjectPro.controller;

import com.ProjectPro.ProjectPro.entity.MessageModel;
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

    @GetMapping
    public String getMessages(Model model, HttpSession session) {
        Integer loggedInUserId = (Integer) session.getAttribute("userId");

        if (loggedInUserId == null) {
            return "redirect:/view";  // Redirect to login if user is not logged in
        }

        User loggedInUser = usersService.findById(loggedInUserId);
        if (loggedInUser == null) {
            return "redirect:/view";  // User not found, redirect to login
        }

        // If the logged-in user is an employee, we fetch messages between the employee and their supervisor
        if (!roleService.isSupervisor(loggedInUser)) {
            // Fetch the supervisor for the employee
            User supervisor = roleService.findSupervisorForUser(loggedInUser);

            if (supervisor != null) {
                // Fetch all messages between the employee and the supervisor (both sent and received)
                List<MessageModel> mainMessages = messageService.findMessagesBetween(loggedInUser, supervisor);

                // Combine main messages and their replies, sort by dateTime
                List<MessageModel> sortedMessages = new ArrayList<>(mainMessages);
                for (MessageModel message : mainMessages) {
                    sortedMessages.addAll(message.getReplies()); // Add replies to each message
                }
                sortedMessages.sort(Comparator.comparing(MessageModel::getDateTime)); // Sort the combined list by dateTime

                model.addAttribute("messages", sortedMessages); // Pass the sorted messages to the view
            } else {
                model.addAttribute("messages", List.of());  // Empty list if no supervisor is found
            }
        }

        model.addAttribute("userId", loggedInUserId);
        return "message/messages";  // Return the view that displays the messages
    }



    @PostMapping("/post")
    public String postMessage(@RequestParam int userId, @RequestParam String message, @RequestParam(required = false) Integer replyId) {
        MessageModel newMessage = new MessageModel();
        User sender = usersService.findById(userId);

        // Find a supervisor for the sender
        User supervisor = roleService.findSupervisorForUser(sender);

        newMessage.setSender(sender);
        newMessage.setReceiver(supervisor);  // Set supervisor as the receiver
        newMessage.setMessage(message);
        newMessage.setDateTime(LocalDateTime.now());

        if (replyId != null) {
            MessageModel reply = messageService.findById(replyId);
            newMessage.setReply(reply);
        }

        messageService.saveMessage(newMessage);
        return "redirect:/messages";
    }

    @GetMapping("/replies/{id}")
    public String getReplies(@PathVariable Integer id, Model model) {
        MessageModel message = messageService.findById(id);
        if (message == null) {
            return "redirect:/messages?error=message_not_found";
        }

        List<MessageModel> replies = messageService.findReplies(message);
        model.addAttribute("message", message);
        model.addAttribute("replies", replies);
        return "message/replies";
    }
}
