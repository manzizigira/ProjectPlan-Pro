package com.ProjectPro.ProjectPro.controller;

import com.ProjectPro.ProjectPro.Dto.ChatMessageDTO;
import com.ProjectPro.ProjectPro.Dto.MessageDTO;
import com.ProjectPro.ProjectPro.Dto.UserChat;
import com.ProjectPro.ProjectPro.entity.*;
import com.ProjectPro.ProjectPro.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

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
    public String hodMessagePage(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        List<MessageDTO> messageDTOs = messageService.findSenderByReceiver(userId);

        // Filter the list to only include unique senderIds
        List<MessageDTO> uniqueMessageDTOs = messageDTOs.stream()
                .filter(distinctByKey(MessageDTO::getSenderId))
                .toList();

        model.addAttribute("chatNames", uniqueMessageDTOs);

        return "message/messagePage";
    }

    // Helper method to filter by a key
    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    @GetMapping("/projectMessage")
    public String projectManagerMessagePage(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        List<MessageDTO> messageDTOs = messageService.findSenderByReceiver(userId);

        // Filter the list to only include unique senderIds
        List<MessageDTO> uniqueMessageDTOs = messageDTOs.stream()
                .filter(distinctByKey(MessageDTO::getSenderId))
                .toList();

        model.addAttribute("chatNames", uniqueMessageDTOs);

        return "message/projectManagerMessagePage";
    }

    @GetMapping("/supervisorMessage")
    public String supervisorMessagePage(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        List<MessageDTO> messageDTOs = messageService.findSenderByReceiver(userId);

        // Filter the list to only include unique senderIds
        List<MessageDTO> uniqueMessageDTOs = messageDTOs.stream()
                .filter(distinctByKey(MessageDTO::getSenderId))
                .toList();

        model.addAttribute("chatNames", uniqueMessageDTOs);

        return "message/supervisorMessagePage";
    }

    @GetMapping("/employeeMessage")
    public String employeeMessagePage(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        List<MessageDTO> messageDTOs = messageService.findSenderByReceiver(userId);

        // Filter the list to only include unique senderIds
        List<MessageDTO> uniqueMessageDTOs = messageDTOs.stream()
                .filter(distinctByKey(MessageDTO::getSenderId))
                .toList();

        model.addAttribute("chatNames", uniqueMessageDTOs);

        return "message/employeeMessagePage";
    }


    @PostMapping("/post")
    public String postMessage(
            @RequestParam("receiverId") Integer receiverId,
            @RequestParam("message") String messageContent,
            HttpSession session) {

        if (messageContent == null || messageContent.trim().isEmpty()) {
            return "redirect:/messages/view?error=Message content cannot be empty";
        }

        // Get the sender (current logged-in user)
        Integer userId = (Integer) session.getAttribute("userId");
        User sender = usersService.findById(userId);

        // Get the receiver
        User receiver = usersService.findById(receiverId);

        // Create the message
        messageService.createMessage(sender, receiver, messageContent, null, null, null);

        return "redirect:/messages/view";
    }

    @PostMapping("/projectPost")
    public String projectPostMessage(
            @RequestParam("receiverId") Integer receiverId,
            @RequestParam("message") String messageContent,
            HttpSession session) {

        if (messageContent == null || messageContent.trim().isEmpty()) {
            return "redirect:/messages/view?error=Message content cannot be empty";
        }

        // Get the sender (current logged-in user)
        Integer userId = (Integer) session.getAttribute("userId");
        User sender = usersService.findById(userId);

        // Get the receiver
        User receiver = usersService.findById(receiverId);

        // Create the message
        messageService.createMessage(sender, receiver, messageContent, null, null, null);

        return "redirect:/messages/projectMessage";
    }

    @PostMapping("/supervisorPost")
    public String supervisorPostMessage(
            @RequestParam("receiverId") Integer receiverId,
            @RequestParam("message") String messageContent,
            HttpSession session) {

        if (messageContent == null || messageContent.trim().isEmpty()) {
            return "redirect:/messages/view?error=Message content cannot be empty";
        }

        // Get the sender (current logged-in user)
        Integer userId = (Integer) session.getAttribute("userId");
        User sender = usersService.findById(userId);

        // Get the receiver
        User receiver = usersService.findById(receiverId);

        // Create the message
        messageService.createMessage(sender, receiver, messageContent, null, null, null);

        return "redirect:/messages/supervisorMessage";
    }

    @PostMapping("/employeePost")
    public String employeeMessagePostMessage(
            @RequestParam("receiverId") Integer receiverId,
            @RequestParam("message") String messageContent,
            HttpSession session) {

        if (messageContent == null || messageContent.trim().isEmpty()) {
            return "redirect:/messages/view?error=Message content cannot be empty";
        }

        // Get the sender (current logged-in user)
        Integer userId = (Integer) session.getAttribute("userId");
        User sender = usersService.findById(userId);

        // Get the receiver
        User receiver = usersService.findById(receiverId);

        // Create the message
        messageService.createMessage(sender, receiver, messageContent, null, null, null);

        return "redirect:/messages/employeeMessage";
    }



    @GetMapping("/chat/{senderId}")
    @ResponseBody
    public Map<String, Object> getChatMessages(@PathVariable("senderId") int senderId, HttpSession session) {
        Integer currentUserId = (Integer) session.getAttribute("userId");

        // Fetch messages between the current user and the sender
        List<ChatMessageDTO> messages = messageService.getChatMessagesBetweenUsers(currentUserId, senderId);

        Map<String, Object> response = new HashMap<>();
        response.put("messages", messages);
        response.put("currentUserId", currentUserId); // Include current user ID for reference on the client side

        return response;
    }


}
