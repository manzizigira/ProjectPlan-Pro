package com.ProjectPro.ProjectPro.repository;

import com.ProjectPro.ProjectPro.entity.Activity;
import com.ProjectPro.ProjectPro.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ActivityRepo extends JpaRepository<Activity, Integer> {
    Activity findByEmployee(Employee employee);
    List<Activity> findByStatus(String status);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END " +
            "FROM Activity a JOIN a.employee e " +
            "WHERE a.id = :activityId AND e.id = :employeeId")
    boolean existsByActivityIdAndEmployeeId(@Param("activityId") int activityId, @Param("employeeId") int employeeId);

    List<Activity> findActivitiesByEmployee(Employee employee);

    @Query(
            """
                SELECT a
                FROM Activity a
                JOIN a.project p
                JOIN p.projectManager pm
                JOIN pm.user u
                WHERE u.id =:userId
            """
    )
    List<Activity> findActivitiesByProjectManagerUserId(@Param("userId") int userId);


    @Query(
            """
                SELECT a
                FROM Activity a
                JOIN a.project p
                JOIN p.directorate d
                JOIN d.headOfDirectorate hd
                JOIN hd.user u
                WHERE u.id =:userId
            """
    )
    List<Activity> findActivitiesByHeadOfDepartmentUserId(@Param("userId") int userId);
}
