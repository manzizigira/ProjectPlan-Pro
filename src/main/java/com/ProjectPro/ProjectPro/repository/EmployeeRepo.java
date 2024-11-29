package com.ProjectPro.ProjectPro.repository;

import com.ProjectPro.ProjectPro.entity.Employee;
import com.ProjectPro.ProjectPro.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepo extends JpaRepository<Employee,Integer> {
    Employee findByUser(User user);
    Employee findByUserId(int userId);

    @Query(
            """
                SELECT e.department
                FROM Employee e
                JOIN e.user u
                WHERE u.id=:userId
            """
    )
    String findDepartmentByUserId(@Param("userId") int userId);

    @Query(
            """
                SELECT e
                FROM Employee e
                WHERE e.department=:department        
            """
    )
    List<Employee> getEmployeesByDepartment(@Param("department") String department);

    @Query(
            """
                SELECT e
                FROM Employee e
                JOIN e.user u
                JOIN u.roles r
                WHERE r.roleName ='SUPERVISOR' AND e.department=:department
            """
    )
    List<Employee> findSupervisorsByDepartment(@Param("department") String department);

    @Query(
            """
                SELECT e
                FROM Employee e
                JOIN e.user u
                JOIN u.roles r
                WHERE r.roleName ='EMPLOYEE' AND e.department=:department
            """
    )
    List<Employee> findEmployeesByDepartment(@Param("department") String department);

    @Query(
            """
                SELECT e
                FROM Employee e
                JOIN e.user u
                JOIN u.roles r
                WHERE r.roleName ='PROJECTMANAGER' AND e.department=:department
            """
    )
    List<Employee> findProjectManagersByDepartment(@Param("department") String department);
}
