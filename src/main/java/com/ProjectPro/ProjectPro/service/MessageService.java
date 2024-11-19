package com.ProjectPro.ProjectPro.service;

import com.ProjectPro.ProjectPro.entity.MessageModel;
import com.ProjectPro.ProjectPro.entity.User;

import java.util.List;

public interface MessageService {

    public List<MessageModel> findAllMessages();

    public List<MessageModel> findAllMessagesForAdmin(User user);

    public List<MessageModel> findReplies(MessageModel message);

    public List<MessageModel> findRepliesForAdmin(MessageModel message);

    public MessageModel saveMessage(MessageModel message);

    public MessageModel findById(int id);

    public void updateMessageVisibility(int id, boolean isVisible);
    public List<MessageModel> findAllMessages(User employee, User supervisor);

    List<MessageModel> findMessagesBetween(User supervisor, User employee);


}
