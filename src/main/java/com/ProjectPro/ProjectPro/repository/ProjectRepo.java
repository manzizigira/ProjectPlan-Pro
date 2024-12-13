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

    @Query("""
                SELECT COUNT(p) 
                FROM Project p 
                JOIN p.directorate d
                JOIN d.headOfDirectorate e
                JOIN e.user u
                WHERE u.id = :hodId
           """)
    int countProjectsByHodId(int hodId);


    @Query("""
           SELECT COUNT(p)
           FROM Project p
           JOIN p.directorate d
           JOIN d.headOfDirectorate e
           JOIN e.user u
           WHERE u.id = :hodId AND p.projectManager IS NOT NULL
       """)
    int findAssignedProjectsByHodId(int hodId);

    @Query("""
       SELECT COUNT(p)
       FROM Project p
       JOIN p.directorate d
       JOIN d.headOfDirectorate e
       JOIN e.user u
       JOIN p.taskManagements t
       WHERE u.id = :hodId AND p.projectManager IS NOT NULL AND t.supervisor IS NOT NULL
       """)
    int countProjectsAssignedByHodWithSupervisors(int hodId);


    @Query("SELECT p FROM Project p WHERE p.directorate.headOfDirectorate.id = :hodId AND p.taskManagements IS NOT EMPTY AND " +
            "EXISTS (SELECT t FROM TaskManagement t WHERE t.project.id = p.id AND t.status = 'Completed')")
    List<Project> findCompletedProjectsByHodId(int hodId);

    @Query("""
           SELECT MONTH(p.startDate), COUNT(p)
           FROM Project p
           WHERE p.directorate.headOfDirectorate.user.id = :userId
           GROUP BY MONTH(p.startDate)
           ORDER BY MONTH(p.startDate)
       """)
    List<Object[]> findMonthlyProjectCreationStatsByHodId(int userId);
}

