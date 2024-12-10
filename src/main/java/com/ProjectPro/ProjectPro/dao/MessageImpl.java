package com.ProjectPro.ProjectPro.dao;

import com.ProjectPro.ProjectPro.Dto.ChatMessageDTO;
import com.ProjectPro.ProjectPro.Dto.MessageDTO;
import com.ProjectPro.ProjectPro.entity.*;
import com.ProjectPro.ProjectPro.repository.EmployeeRepo;
import com.ProjectPro.ProjectPro.repository.MessageRepository;
import com.ProjectPro.ProjectPro.repository.RoleRepo;
import com.ProjectPro.ProjectPro.repository.UserRepo;
import com.ProjectPro.ProjectPro.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageImpl implements MessageService {

    private MessageRepository messageRepository;
    private UserRepo userRepo;
    private EmployeeRepo employeeRepo;
    private RoleRepo roleRepo;

    @Autowired
    public MessageImpl(MessageRepository messageRepository, UserRepo userRepo, EmployeeRepo employeeRepo, RoleRepo roleRepo) {
        this.messageRepository = messageRepository;
        this.userRepo = userRepo;
        this.employeeRepo = employeeRepo;
        this.roleRepo = roleRepo;
    }

    @Override
    public List<MessageModel> findBySenderIdAndProjectNotNull(Integer senderId) {
        return messageRepository.findBySenderIdAndProjectNotNull(senderId);
    }

    @Override
    public List<MessageModel> findByReceiverIdAndProjectNotNull(Integer receiverId) {
        return messageRepository.findByReceiverIdAndProjectNotNull(receiverId);
    }

    @Override
    public List<MessageModel> findByReceiverIdAndTaskNotNull(Integer receiverId) {
        return messageRepository.findByReceiverIdAndTaskNotNull(receiverId);
    }

    @Override
    public List<MessageModel> findBySenderIdAndTaskNotNull(Integer senderId) {
        return messageRepository.findBySenderIdAndTaskNotNull(senderId);
    }

    @Override
    public List<MessageModel> findByProjectIdAndReceiverRole(Integer projectId, Role role) {
        return messageRepository.findByProjectIdAndReceiverRole(projectId,role);
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
    public MessageModel getMessageById(int messageId) {
        return messageRepository.findById(messageId).orElse(null);
    }

    @Override
    public void createProjectAssignmentMessage(User sender, User receiver, Project project, Role senderRole, Role receiverRole) {
        String messageContent = "You have been assigned a new project: " + project.getName();
        MessageModel message = new MessageModel(sender, receiver, messageContent, LocalDateTime.now(), MessageStatus.SENT);
        message.setProject(project); // Set the project in the message
        message.setSenderRole(senderRole); // Set the sender's role
        message.setReceiverRole(receiverRole); // Set the receiver's role
        messageRepository.save(message);
    }

    @Override
    public void createTaskToSupervisorAssignmentMessage(User sender, User receiver, TaskManagement taskManagement, Role senderRole, Role receiverRole) {
        String messageContent = "You have been assigned a new task: " + taskManagement.getName();
        MessageModel message = new MessageModel(sender, receiver, messageContent, LocalDateTime.now(), MessageStatus.SENT);
        message.setTask(taskManagement); // Set the project in the message
        message.setSenderRole(senderRole); // Set the sender's role
        message.setReceiverRole(receiverRole); // Set the receiver's role
        messageRepository.save(message);
    }

    @Override
    public void createSupervisorToTaskLeaderAssignmentMessage(User sender, User receiver, TaskManagement taskManagement, Role senderRole, Role receiverRole) {
        String messageContent = "You have been assigned a new task as a task leader: " + taskManagement.getName();
        MessageModel message = new MessageModel(sender, receiver, messageContent, LocalDateTime.now(), MessageStatus.SENT);
        message.setTask(taskManagement); // Set the project in the message
        message.setSenderRole(senderRole); // Set the sender's role
        message.setReceiverRole(receiverRole); // Set the receiver's role
        messageRepository.save(message);
    }

    @Override
    public void createActivityAssignmentMessage(User sender, User receiver, Activity activity, Role senderRole, Role receiverRole) {
        String messageContent = "You have been assigned a new activity: " + activity.getActivityName();
        MessageModel message = new MessageModel(sender, receiver, messageContent, LocalDateTime.now(), MessageStatus.SENT);
        message.setActivity(activity); // Set the project in the message
        message.setSenderRole(senderRole); // Set the sender's role
        message.setReceiverRole(receiverRole); // Set the receiver's role
        messageRepository.save(message);
    }

    @Override
    public void createAssignmentMessageBetweenTaskLeaderAndEmployees(User sender, User receivers, TaskManagement taskManagement, Role senderRole, Role receiverRole) {
        String messageContent = "We have been assigned a new Task: " + taskManagement.getName() + " and i'm your Task Leader "+taskManagement.getSupervisor().getName();
        MessageModel message = new MessageModel(sender, receivers, messageContent, LocalDateTime.now(), MessageStatus.SENT);
        message.setTask(taskManagement); // Set the project in the message
        message.setSenderRole(senderRole); // Set the sender's role
        message.setReceiverRole(receiverRole); // Set the receiver's role
        messageRepository.save(message);
    }

    @Override
    public List<MessageModel> getReceiverMessages(int user) {
        return messageRepository.findReceiverMessages(user);
    }

    @Override
    public List<MessageDTO> findSenderByReceiver(int receiverId) {
        return messageRepository.findSenderByReceiver(receiverId);
    }

    @Override
    public List<ChatMessageDTO> getChatMessagesBetweenUsers(int currentUserId, int senderId) {
        List<MessageModel> messages = messageRepository.findMessagesBetweenUsers(currentUserId, senderId);

        return messages.stream()
                .map(message -> new ChatMessageDTO(
                        message.getSender().getId(),
                        message.getReceiver().getId(),
                        message.getSender().getEmployee().getName(),
                        message.getReceiver().getEmployee().getName(),
                        message.getMessageContent(),
                        message.getTimestamp()
                ))
                .collect(Collectors.toList());
    }


}
