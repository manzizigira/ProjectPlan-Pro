package com.ProjectPro.ProjectPro.dao;

import com.ProjectPro.ProjectPro.entity.*;
import com.ProjectPro.ProjectPro.repository.MessageRepository;
import com.ProjectPro.ProjectPro.repository.UserRepo;
import com.ProjectPro.ProjectPro.service.MessageService;
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
    public void createChatIfNotExists(User sender, User receiver) {
        boolean chatExists = messageRepository.existsBySenderIdAndReceiverId(sender.getId(), receiver.getId());
        if (!chatExists) {
            // Create a new chat message
            MessageModel message = new MessageModel();
            message.setSender(sender);
            message.setReceiver(receiver);
            message.setMessageContent("Project assigned");
            message.setTimestamp(LocalDateTime.now());
            messageRepository.save(message);
        }
    }

    @Override
    public List<MessageModel> findMessagesByChatId(int senderId, int receiverId) {
        return messageRepository.findMessagesBySenderAndReceiver(senderId, receiverId);
    }

}
