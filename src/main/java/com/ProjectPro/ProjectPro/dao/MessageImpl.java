package com.ProjectPro.ProjectPro.dao;

import com.ProjectPro.ProjectPro.entity.MessageModel;
import com.ProjectPro.ProjectPro.entity.User;
import com.ProjectPro.ProjectPro.repository.MessageRepository;
import com.ProjectPro.ProjectPro.repository.RoleRepo;
import com.ProjectPro.ProjectPro.repository.UserRepo;
import com.ProjectPro.ProjectPro.service.MessageService;
import com.ProjectPro.ProjectPro.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageImpl implements MessageService {

    private MessageRepository messageRepository;
    private UserRepo userRepo;
    private RoleRepo roleRepo;
    private RoleService roleService;

    @Autowired
    public MessageImpl(MessageRepository messageRepository, UserRepo userRepo, RoleRepo roleRepo, RoleService roleService) {
        this.messageRepository = messageRepository;
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.roleService = roleService;
    }

    @Override
    public List<MessageModel> findAllMessages() {
        return messageRepository.findAllByReplyIsNullAndIsVisibleTrueOrderByDateTimeDesc();
    }

    @Override
    public List<MessageModel> findAllMessagesForAdmin(User user) {
        return messageRepository.findAllByReceiverAndReplyIsNullOrderByDateTimeDesc(user);
    }

    @Override
    public List<MessageModel> findReplies(MessageModel message) {
        return messageRepository.findAllByReplyAndIsVisibleTrueOrderByDateTimeAsc(message);
    }

    @Override
    public List<MessageModel> findRepliesForAdmin(MessageModel message) {
        return messageRepository.findAllByReplyOrderByDateTimeAsc(message);
    }

    @Override
    public MessageModel saveMessage(MessageModel message) {
        return messageRepository.save(message);
    }

    @Override
    public MessageModel findById(int id) {
        return messageRepository.findById(id).orElse(null);
    }

    @Override
    public void updateMessageVisibility(int id, boolean isVisible) {
        MessageModel message = findById(id);
        if (message != null) {
            message.setVisible(isVisible);
            messageRepository.save(message);
        }
    }

    @Override
    public List<MessageModel> findAllMessages(User employee, User supervisor) {
        return messageRepository.findAllBySenderAndReceiverAndReplyIsNullAndIsVisibleTrueOrderByDateTimeDesc(employee, supervisor);
    }

    @Override
    public List<MessageModel> findMessagesBetween(User supervisor, User employee) {
        return messageRepository.findAllBySenderAndReceiver(supervisor, employee);
    }

}
