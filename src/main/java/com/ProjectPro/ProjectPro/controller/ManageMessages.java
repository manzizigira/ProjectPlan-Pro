package com.ProjectPro.ProjectPro.controller;

import com.ProjectPro.ProjectPro.entity.Employee;
import com.ProjectPro.ProjectPro.entity.MessageModel;
import com.ProjectPro.ProjectPro.entity.User;
import com.ProjectPro.ProjectPro.service.MessageService;
import com.ProjectPro.ProjectPro.service.UsersService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@Controller
@RequestMapping("/manageMessages")
public class ManageMessages {

    private MessageService messageService;
    private UsersService usersService;

    @Autowired
    public ManageMessages(MessageService messageService, UsersService usersService) {
        this.messageService = messageService;
        this.usersService = usersService;
    }

    @GetMapping
    public String getMessages(Model model, HttpSession session, @RequestParam(required = false) Integer employeeId) {
        Integer loggedInUserId = (Integer) session.getAttribute("userId");

        if (loggedInUserId == null) {
            return "redirect:/view";  // Redirect if not logged in
        }

        User loggedInUser = usersService.findById(loggedInUserId);
        if (loggedInUser == null) {
            return "redirect:/view";  // User not found
        }

        // Get all employees for the employee list in the sidebar
        List<Employee> employees = usersService.findAllEmployees();
        model.addAttribute("employees", employees);

        List<MessageModel> sortedMessages = new ArrayList<>();

        // Check if a specific employee is selected
        if (employeeId != null) {
            User selectedEmployee = usersService.findById(employeeId); // Use employeeId to load selected user
            List<MessageModel> mainMessages = messageService.findMessagesBetween(loggedInUser, selectedEmployee);

            // Combine main messages and their replies
            sortedMessages.addAll(mainMessages);
            for (MessageModel message : mainMessages) {
                sortedMessages.addAll(message.getReplies());
            }

            // Sort the combined list by dateTime
            sortedMessages.sort(Comparator.comparing(MessageModel::getDateTime));

            model.addAttribute("messages", sortedMessages);
            model.addAttribute("selectedEmployeeId", employeeId);
        }

        model.addAttribute("userId", loggedInUserId);
        return "message/ManageMessages";
    }



    @PostMapping("/post")
    public String postMessage(@RequestParam int userId,
                              @RequestParam String message,
                              @RequestParam(required = false) Integer replyId,
                              @RequestParam(required = false) Integer receiverId) {

        MessageModel newMessage = new MessageModel();
        User sender = usersService.findById(userId);  // The supervisor
        User receiver = null;  // This will be the employee

        // If this is a reply to an existing message, get the receiver from the original message's sender
        if (replyId != null) {
            MessageModel originalMessage = messageService.findById(replyId);
            if (originalMessage != null) {
                receiver = originalMessage.getSender();  // The employee who sent the original message
                newMessage.setReply(originalMessage);    // Set the original message as a reply
            }
        }

        // If no replyId is provided, this is a new message, use the provided receiverId
        if (receiver == null && receiverId != null) {
            receiver = usersService.findById(receiverId);  // Assign the receiver based on form input
        }

        // Make sure we have both sender and receiver before saving
        if (sender == null || receiver == null) {
            // Handle the error appropriately, either log or show a message to the user
            return "redirect:/manageMessages?error=missingUser";
        }

        newMessage.setSender(sender);   // The supervisor is sending the message
        newMessage.setReceiver(receiver); // The employee receiving the message
        newMessage.setMessage(message);
        newMessage.setDateTime(LocalDateTime.now());

        messageService.saveMessage(newMessage);
        return "redirect:/manageMessages?employeeId=" + receiver.getId();  // Reload the page with selected employee
    }

    @PostMapping("/updateVisibility")
    public String updateMessageVisibility(@RequestParam("id") Integer id, @RequestParam("isVisible") boolean isVisible) {
        messageService.updateMessageVisibility(id, isVisible);
        return "redirect:/manageMessages";
    }
}