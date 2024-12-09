package com.ProjectPro.ProjectPro.Dto;

import java.time.LocalDateTime;

public class ChatMessageDTO {

    private int senderId; // Ensure IDs are included
    private int receiverId;
    private String senderName;
    private String receiverName;
    private String messageContent;
    private LocalDateTime timestamp;

    public ChatMessageDTO(int senderId, int receiverId, String senderName, String receiverName, String messageContent, LocalDateTime timestamp) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.messageContent = messageContent;
        this.timestamp = timestamp;
    }

    // Getters and setters
    public String getSenderName() {
        return senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
