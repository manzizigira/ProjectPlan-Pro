package com.ProjectPro.ProjectPro.entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class MessageModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender; // The user who sends the message

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver; // The user who receives the message

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project; // The project related to the message (can be null)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private TaskManagement task; // The task related to the message (can be null)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id")
    private Activity activity; // The activity related to the message (can be null)

    private String messageContent; // The content of the message

    private LocalDateTime timestamp; // Timestamp when the message is sent

    @Enumerated(EnumType.STRING)
    private MessageStatus status; // MessageModel status, e.g., SENT, READ, etc.

    @ManyToOne
    @JoinColumn(name = "sender_role_id")
    private Role senderRole;

    @ManyToOne
    @JoinColumn(name = "receiver_role_id")
    private Role receiverRole;

    // Constructor, Getters, Setters

    public MessageModel() {}

    public MessageModel(User sender, User receiver, String messageContent, LocalDateTime timestamp, MessageStatus status) {
        this.sender = sender;
        this.receiver = receiver;
        this.messageContent = messageContent;
        this.timestamp = timestamp;
        this.status = status;
    }

    // Getters and Setters...

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public TaskManagement getTask() {
        return task;
    }

    public void setTask(TaskManagement task) {
        this.task = task;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public Employee getSenderEmployee() {
        return sender != null ? sender.getEmployee() : null;
    }

    public Employee getReceiverEmployee() {
        return receiver != null ? receiver.getEmployee() : null;
    }

    public Role getSenderRole() {
        return senderRole;
    }

    public void setSenderRole(Role senderRole) {
        this.senderRole = senderRole;
    }

    public Role getReceiverRole() {
        return receiverRole;
    }

    public void setReceiverRole(Role receiverRole) {
        this.receiverRole = receiverRole;
    }
}
