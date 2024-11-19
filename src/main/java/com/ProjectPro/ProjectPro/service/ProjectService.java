package com.ProjectPro.ProjectPro.service;

import com.ProjectPro.ProjectPro.entity.Project;

import java.util.List;

public interface ProjectService {

    Project save(Project project);

    Project findById(int theId);

    void delete(int theId);

    List<Project> findAll();



}
