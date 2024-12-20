package com.ProjectPro.ProjectPro.service;

import com.ProjectPro.ProjectPro.Dto.ChatMessageDTO;
import com.ProjectPro.ProjectPro.Dto.MessageDTO;
import com.ProjectPro.ProjectPro.entity.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageService {

    // Find messages for HOD (HOD sends to Project Manager, etc.)
    List<MessageModel> findBySenderIdAndProjectNotNull(Integer senderId);

    // Find messages for Project Manager
    List<MessageModel> findByReceiverIdAndProjectNotNull(Integer receiverId);

    // Find messages for Supervisor or Employee
    List<MessageModel> findByReceiverIdAndTaskNotNull(Integer receiverId);

    // Find messages for a task related to the current employee
    List<MessageModel> findBySenderIdAndTaskNotNull(Integer senderId);

    // Custom query for checking chats involving a project assignment (HOD -> Project Manager)
    List<MessageModel> findByProjectIdAndReceiverRole(Integer projectId, Role role);


    void createMessage(User sender, User receiver, String messageContent, Project project, TaskManagement task, Activity activity);

    void createTaskMessage(User projectManager, User supervisor, TaskManagement task);

    MessageModel getMessageById(int messageId);

    void createProjectAssignmentMessage(User sender, User receiver, Project project, Role senderRole, Role receiverRole);
    void createTaskToSupervisorAssignmentMessage(User sender, User receiver, TaskManagement taskManagement, Role senderRole, Role receiverRole);
    void createSupervisorToTaskLeaderAssignmentMessage(User sender, User receiver, TaskManagement taskManagement, Role senderRole, Role receiverRole);
    void createActivityAssignmentMessage(User sender, User receiver, Activity activity, Role senderRole, Role receiverRole);
    void createAssignmentMessageBetweenTaskLeaderAndEmployees(User sender, User receiver, TaskManagement taskManagement, Role senderRole, Role receiverRole);

    List<MessageModel> getReceiverMessages(int user);
    List<MessageDTO> findSenderByReceiver(@Param("receiverId") int receiverId);

    List<ChatMessageDTO> getChatMessagesBetweenUsers(int currentUserId, int senderId);

}
