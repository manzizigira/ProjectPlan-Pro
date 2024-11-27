package com.ProjectPro.ProjectPro.dao;

import com.ProjectPro.ProjectPro.entity.Project;
import com.ProjectPro.ProjectPro.repository.ProjectRepo;
import com.ProjectPro.ProjectPro.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectImpl implements ProjectService {

    private ProjectRepo projectRepo;

    @Autowired
    public ProjectImpl(ProjectRepo projectRepo) {
        this.projectRepo = projectRepo;
    }

    @Override
    public Project save(Project project) {
        return projectRepo.save(project);
    }

    @Override
    public Project findById(int theId) {
        Optional<Project> result = projectRepo.findById(theId);
        Project project = null;
        if (result.isPresent()){
            project = result.get();
        }else {
            throw new RuntimeException("Id not Found!");
        }
        return project;
    }

    @Override
    public void delete(int theId) {
        projectRepo.deleteById(theId);
    }

    @Override
    public List<Project> findAll() {
        return projectRepo.findAll();
    }

    @Override
    public List<Project> findProjectsByUserId(int userId) {
        return projectRepo.findProjectsByUserId(userId);
    }

    @Override
    public List<Project> findProjectsByDirectorateUserId(int userId) {
        return projectRepo.findProjectsByDirectorateUserId(userId);
    }


}
