package com.ProjectPro.ProjectPro.repository;

import com.ProjectPro.ProjectPro.entity.Employee;
import com.ProjectPro.ProjectPro.entity.MessageModel;
import com.ProjectPro.ProjectPro.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Integer> {
    User findByEmail(String username);

    User findByEmployee(Employee employee);

    User findUsersByMessageSupervisor(List<MessageModel> messageModel);
}
