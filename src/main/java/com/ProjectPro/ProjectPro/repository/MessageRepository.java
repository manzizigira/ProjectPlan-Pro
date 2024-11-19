package com.ProjectPro.ProjectPro.repository;

import com.ProjectPro.ProjectPro.entity.MessageModel;
import com.ProjectPro.ProjectPro.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageModel, Integer> {
    @EntityGraph(attributePaths = {"replies", "replies.user", "replies.replies"})
    List<MessageModel> findAllByReplyIsNullAndIsVisibleTrueOrderByDateTimeDesc();

    List<MessageModel> findAllByReplyAndIsVisibleTrueOrderByDateTimeAsc(MessageModel reply);

    @EntityGraph(attributePaths = {"replies", "replies.user", "replies.replies"})
    List<MessageModel> findAllByReplyIsNullOrderByDateTimeDesc(); // For admin to see all messages

    List<MessageModel> findAllByReplyOrderByDateTimeAsc(MessageModel reply); // For admin to see all replies

    @EntityGraph(attributePaths = {"replies", "replies.sender", "replies.replies"})
    List<MessageModel> findAllBySenderAndReceiverAndReplyIsNullAndIsVisibleTrueOrderByDateTimeDesc(User sender, User receiver);

    @Query("SELECT m FROM MessageModel m WHERE (m.sender = :user1 AND m.receiver = :user2) OR (m.sender = :user2 AND m.receiver = :user1)")
    List<MessageModel> findAllBySenderAndReceiver(@Param("user1") User user1, @Param("user2") User user2);


    @EntityGraph(attributePaths = {"replies", "replies.sender", "replies.replies"})
    List<MessageModel> findAllByReceiverAndSenderAndReplyIsNullAndIsVisibleTrueOrderByDateTimeDesc(User receiver, User sender);

    List<MessageModel> findAllByReceiverAndReplyIsNullOrderByDateTimeDesc(User receiver);

    @Query("SELECT m FROM MessageModel m WHERE (m.sender = :supervisor AND m.receiver = :employee) " +
            "OR (m.sender = :employee AND m.receiver = :supervisor) ORDER BY m.dateTime ASC")
    List<MessageModel> findMessagesBetweenUsers(@Param("supervisor") User supervisor, @Param("employee") User employee);

}

