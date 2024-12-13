package com.ProjectPro.ProjectPro.service;

import com.ProjectPro.ProjectPro.entity.Project;

import java.util.List;

public interface ProjectService {

    Project save(Project project);

    Project findById(int theId);

    void delete(int theId);

    List<Project> findAll();

    List<Project> findProjectsByUserId(int userId);

    List<Project> findProjectsByDirectorateUserId(int userId);

    int getProjectsCreatedByHod(int hodId);

    int  getAssignedProjectsByHod(int hodId);

    int countProjectsAssignedByHodWithSupervisors(int hodId);

    List<Project> getCompletedProjectsByHod(int hodId);

    List<Object[]> getMonthlyProjectStats(int hodId);

}
