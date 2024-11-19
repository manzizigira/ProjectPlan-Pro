package com.ProjectPro.ProjectPro.repository;

import com.ProjectPro.ProjectPro.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepo extends JpaRepository<Project, Integer> {

}
