package com.ProjectPro.ProjectPro.repository;

import com.ProjectPro.ProjectPro.entity.Employee;
import com.ProjectPro.ProjectPro.entity.TaskManagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskManagementRepo extends JpaRepository<TaskManagement, Integer> {
    List<TaskManagement> findTaskManagementsByEmployeesId(int theId);

    @Query("select tm from TaskManagement tm JOIN FETCH tm.employees WHERE tm.id= :theId")
    TaskManagement findTaskManagementAndEmployeeByTaskManagementId(@Param("theId") int theId);

    List<TaskManagement> findTaskManagementIdByEmployeesId(int theId);

    List<TaskManagement> findTaskManagementsByProjectId(int theId);

    List<TaskManagement> findByEmployees(Employee employee);

    List<TaskManagement> findByStatus(String status);

    @Query("""
        SELECT t
        FROM TaskManagement t
        JOIN t.project p
        JOIN p.projectManager e
        JOIN e.user u
        WHERE u.id =:userId
    """)
    List<TaskManagement> findTaskManagementsByProjectManagerUserId(@Param("userId") int userId);

}

