package com.ProjectPro.ProjectPro.repository;

import com.ProjectPro.ProjectPro.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Integer> {
    User findByEmail(String username);
}
