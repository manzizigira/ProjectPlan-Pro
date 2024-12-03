package com.ProjectPro.ProjectPro.service;

import com.ProjectPro.ProjectPro.entity.*;

import java.util.List;

public interface MessageService {

    List<MessageModel> getMessagesForHOD(User hod);

    // Messages related to tasks created by Project Manager
    List<MessageModel> getMessagesForProjectManager(User projectManager);

    // Messages related to tasks assigned by Supervisor
    List<MessageModel> getMessagesForSupervisor(User supervisor);

    // Messages for Employee (either task or activity related)
    List<MessageModel> getMessagesForEmployee(User employee);
    void createMessage(User sender, User receiver, String messageContent, Project project, TaskManagement task, Activity activity);

    void createTaskMessage(User projectManager, User supervisor, TaskManagement task);

    public MessageModel getMessageById(Long messageId);


}
