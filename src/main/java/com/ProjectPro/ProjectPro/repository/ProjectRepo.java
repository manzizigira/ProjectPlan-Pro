package com.ProjectPro.ProjectPro.repository;

import com.ProjectPro.ProjectPro.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepo extends JpaRepository<Project, Integer> {

    @Query("""
        SELECT p
        FROM Project p
        JOIN p.projectManager e
        JOIN e.user u
        WHERE u.id =:userId
    """)
    List<Project> findProjectsByUserId(@Param("userId") int userId);

    @Query("""
        SELECT pr
        FROM Project pr
        JOIN pr.directorate d
        JOIN d.headOfDirectorate e
        JOIN e.user u
        WHERE u.id =:userId
    """)
    List<Project> findProjectsByDirectorateUserId(@Param("userId") int userId);
}

