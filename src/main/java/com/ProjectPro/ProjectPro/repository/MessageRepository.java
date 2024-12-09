package com.ProjectPro.ProjectPro.repository;

import com.ProjectPro.ProjectPro.Dto.MessageDTO;
import com.ProjectPro.ProjectPro.entity.MessageModel;
import com.ProjectPro.ProjectPro.entity.Role;
import com.ProjectPro.ProjectPro.entity.User;
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

    @Query
    ("""
        SELECT m
        FROM MessageModel m
        JOIN m.receiver r
        WHERE r.id=:userId
    """)
    List<MessageModel> findReceiverMessages(@Param("userId") int user);

    @Query
    ("""
        SELECT new com.ProjectPro.ProjectPro.Dto.MessageDTO(m.sender.id, m.sender.employee.name, m.timestamp)
        FROM MessageModel m
        JOIN m.sender s
        WHERE m.receiver.id=:receiverId
    """)
    List<MessageDTO> findSenderByReceiver(@Param("receiverId") int receiverId);

    @Query("""
        SELECT m
        FROM MessageModel m
        WHERE (m.sender.id = :senderId AND m.receiver.id = :receiverId) 
           OR (m.sender.id = :receiverId AND m.receiver.id = :senderId)
        ORDER BY m.timestamp ASC
    """)
    List<MessageModel> findChatMessages(@Param("senderId") int senderId, @Param("receiverId") int receiverId);

    @Query("""
    SELECT m
    FROM MessageModel m
    WHERE (m.sender.id = :userId1 AND m.receiver.id = :userId2)
       OR (m.sender.id = :userId2 AND m.receiver.id = :userId1)
    ORDER BY m.timestamp ASC
""")
    List<MessageModel> findMessagesBetweenUsers(@Param("userId1") int userId1, @Param("userId2") int userId2);





}
