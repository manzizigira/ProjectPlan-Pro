package com.ProjectPro.ProjectPro.Dto;

import java.time.LocalDateTime;

public class MessageDTO {
    private int senderId;
    private int receiverId;
    private String senderName;
    private LocalDateTime timestamp;

    public MessageDTO(int senderId, String senderName, LocalDateTime timestamp) {
        this.senderId = senderId;
        this.senderName = senderName;
        this.timestamp = timestamp;
    }

    public String getSenderName() {
        return senderName;
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

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
