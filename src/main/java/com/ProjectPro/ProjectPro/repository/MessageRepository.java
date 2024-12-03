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
public interface MessageRepository extends JpaRepository<MessageModel, Long> {

    // Messages related to projects created by HOD
    List<MessageModel> findMessagesByProjectCreator(User hod);

    // Messages related to tasks created by Project Manager
    List<MessageModel> findMessagesByTaskCreator(User projectManager);

    // Messages related to tasks assigned by Supervisor
    List<MessageModel> findMessagesByTaskAssigner(User supervisor);

    // Messages for Employee (either task or activity related)
    List<MessageModel> findMessagesForEmployee(User employee);

}

