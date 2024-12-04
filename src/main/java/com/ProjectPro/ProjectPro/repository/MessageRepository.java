package com.ProjectPro.ProjectPro.repository;

import com.ProjectPro.ProjectPro.entity.MessageModel;
import com.ProjectPro.ProjectPro.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageModel, Integer> {

    // Find messages for HOD (HOD sends to Project Manager, etc.)
    List<MessageModel> findBySenderIdAndProjectNotNull(Integer senderId);

    // Find messages for Project Manager
    List<MessageModel> findByReceiverIdAndProjectNotNull(Integer receiverId);

    // Find messages for Supervisor or Employee
    List<MessageModel> findByReceiverIdAndTaskNotNull(Integer receiverId);

    // Find messages for a task related to the current employee
    List<MessageModel> findBySenderIdAndTaskNotNull(Integer senderId);

    // Custom query for checking chats involving a project assignment (HOD -> Project Manager)
    public List<MessageModel> findByProjectIdAndReceiverRole(Integer projectId, Role role);

    boolean existsBySenderIdAndReceiverId(Integer senderId, Integer receiverId);

    @Query("SELECT m FROM MessageModel m WHERE (m.sender.id = :senderId AND m.receiver.id = :receiverId) OR (m.sender.id = :receiverId AND m.receiver.id = :senderId)")
    List<MessageModel> findMessagesBySenderAndReceiver(@Param("senderId") int senderId, @Param("receiverId") int receiverId);


}
