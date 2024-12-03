package com.ProjectPro.ProjectPro.dao;

import com.ProjectPro.ProjectPro.entity.*;
import com.ProjectPro.ProjectPro.repository.MessageRepository;
import com.ProjectPro.ProjectPro.repository.RoleRepo;
import com.ProjectPro.ProjectPro.repository.UserRepo;
import com.ProjectPro.ProjectPro.service.MessageService;
import com.ProjectPro.ProjectPro.service.RoleService;
import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageImpl implements MessageService {

    private MessageRepository messageRepository;
    private UserRepo userRepo;

    @Autowired
    public MessageImpl(MessageRepository messageRepository, UserRepo userRepo) {
        this.messageRepository = messageRepository;
        this.userRepo = userRepo;
    }

    @Override
    public List<MessageModel> getMessagesForHOD(User hod) {
        return messageRepository.findMessagesByProjectCreator(hod);
    }

    @Override
    public List<MessageModel> getMessagesForProjectManager(User projectManager) {
        return messageRepository.findMessagesByTaskCreator(projectManager);
    }

    @Override
    public List<MessageModel> getMessagesForSupervisor(User supervisor) {
        return messageRepository.findMessagesByTaskAssigner(supervisor);
    }

    @Override
    public List<MessageModel> getMessagesForEmployee(User employee) {
        return messageRepository.findMessagesForEmployee(employee);
    }

    public void createMessage(User sender, User receiver, String messageContent, Project project, TaskManagement task, Activity activity) {
        MessageModel message = new MessageModel(sender, receiver, messageContent, LocalDateTime.now(), MessageStatus.SENT);
        if (project != null) {
            message.setProject(project);
        }
        if (task != null) {
            message.setTask(task);
        }
        if (activity != null) {
            message.setActivity(activity);
        }
        messageRepository.save(message);
    }

    // Example method to send a message when a project is assigned
    public void createProjectMessage(User hod, User projectManager, Project project) {
        String messageContent = "New project assigned: " + project.getName();
        createMessage(hod, projectManager, messageContent, project, null, null);
    }

    // Example method to send a message when a task is assigned
    public void createTaskMessage(User projectManager, User supervisor, TaskManagement task) {
        String messageContent = "New task assigned: " + task.getName();
        createMessage(projectManager, supervisor, messageContent, null, task, null);
    }

    @Override
    public MessageModel getMessageById(Long messageId) {
        return messageRepository.findById(messageId).orElse(null);
    }

}
