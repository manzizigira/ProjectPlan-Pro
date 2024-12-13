package com.ProjectPro.ProjectPro.repository;

import com.ProjectPro.ProjectPro.Dto.*;
import com.ProjectPro.ProjectPro.entity.Activity;
import com.ProjectPro.ProjectPro.entity.Employee;
import com.ProjectPro.ProjectPro.entity.Report;
import com.ProjectPro.ProjectPro.entity.TaskManagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface ReportRepo extends JpaRepository<Report, Integer> {
    @Query("""
        SELECT COUNT(r) 
        FROM Report r 
        LEFT JOIN Grading g ON r.id = g.report.id 
        WHERE g.id IS NULL
        AND (
            (r.taskManagement IS NOT NULL AND r.taskManagement.status != 'Completed') 
            OR (r.activity IS NOT NULL AND r.activity.status != 'Completed')
        )
    """)
    int countPendingReports();

    @Query("SELECT r.id FROM Report r LEFT JOIN Grading g ON r.id = g.report.id WHERE g.id IS NULL")
    List<Integer> findPendingReportIds();

    List<Report> findAllByEmployeeId(int employeeId);

    @Query("""
        SELECT COUNT(r) 
        FROM Report r 
        LEFT JOIN Grading g ON r.id = g.report.id 
        WHERE g.id IS NOT NULL
        OR (r.taskManagement IS NOT NULL AND r.taskManagement.status = 'Completed')
        OR (r.activity IS NOT NULL AND r.activity.status = 'Completed')
    """)
    int countGradedReports();

    @Query("""
        SELECT new com.ProjectPro.ProjectPro.Dto.PendingSupervisorReportDTO(
            r.id,
            r.reportDescription,
            t.id,
            t.name,
            t.supervisor.name,
            r.submissionDate,
            CASE
                when r.grading is NULL THEN 'Pending'
                ELSE 'Reviewed'
            END
        )
        From Report r
        JOIN r.taskManagement t
        JOIN r.employee e
        JOIN e.user u
        JOIN u.roles ur
        WHERE ur.roleName = 'SUPERVISOR'        
    """)
    List<PendingSupervisorReportDTO> findPendingReportsBySupervisors();

    @Query("""
    SELECT new com.ProjectPro.ProjectPro.Dto.PendingReportDTO(
        r.id,
        r.reportDescription,
        t.id,
        t.name,
        t.taskLeader.name,
        r.submissionDate,
        CASE
            WHEN r.grading IS NULL THEN 'Pending'
            ELSE 'Reviewed'
        END,
        e.id
    )
    FROM Report r
    JOIN r.taskManagement t
    JOIN r.employee e
    JOIN e.user u
    JOIN u.roles ur
    WHERE ur.roleName = 'EMPLOYEE'
""")
    List<PendingReportDTO> findPendingReportsByTaskLeader();



    @Query("""
        SELECT r.id as reportId,
            r.reportDescription as reportTitle,
            t.name as taskName,
            a.activityName as activityName,
            e.name as employeeName,
            r.submissionDate as submissionDate,
            CASE
                when r.grading is NULL THEN 'Pending'
                ELSE 'Reviewed'
            END AS status
        From Report r
        JOIN r.taskManagement t
        JOIN r.activity a
        JOIN r.employee e
        JOIN e.user u
        JOIN u.roles ur
        WHERE ur.roleName = 'EMPLOYEE'        
    """)
    List<Map<String,Object>> findPendingReportsByEmployees();

    @Query("""
        SELECT new com.ProjectPro.ProjectPro.Dto.PendingManagerReportDTO(
            
            r.id,
            r.reportDescription,
            t.project.name,
            t.project.projectManager.name,
            r.submissionDate as submissionDate,
            CASE
                when r.grading is NULL THEN 'Pending'
                ELSE 'Reviewed'
            END AS status
            
        )
        From Report r
        JOIN r.taskManagement t
        JOIN r.employee e
        JOIN e.user u
        JOIN u.roles ur
        WHERE ur.roleName = 'PROJECTMANAGER' AND e.department = 'Water'        
    """)
    List<PendingManagerReportDTO> findPendingReportsByProjectManagers();

    @Query(
            """
                SELECT new com.ProjectPro.ProjectPro.Dto.PendingTaskLeaderReport(
                    r.id,
                    r.reportDescription,
                    t.name,
                    e.name,
                    r.submissionDate
                )
                FROM Report r
                JOIN r.taskManagement t
                JOIN t.taskLeader e
                JOIN e.user u
                WHERE u.id =:userId

            """
    )
    List<PendingTaskLeaderReport> findReportsByEmployeesForTaskLeader(@Param("userId") int taskLeaderId);

    @Query("SELECT r FROM Report r WHERE r.taskManagement = :task AND r.employee = :employee ORDER BY r.submissionDate DESC")
    Report findTopByTaskManagementAndEmployeeOrderBySubmissionDateDesc(
            @Param("task") TaskManagement task,
            @Param("employee") Employee employee);

    @Query("SELECT r FROM Report r WHERE r.activity = :activity AND r.employee = :employee ORDER BY r.submissionDate DESC")
    Report findTopByActivityAndEmployeeOrderBySubmissionDateDesc(
            @Param("activity") Activity activity,
            @Param("employee") Employee employee);

    @Query("SELECT r FROM Report r WHERE r.employee = :employee ORDER BY r.submissionDate DESC")
    Report findTopByEmployeeOrderBySubmissionDateDesc(
            @Param("employee") Employee employee);

    @Query("SELECT r FROM Report r WHERE r.taskManagement.supervisor.user.id = :userId")
    List<Report> findReportsByTaskLeaderUserId(@Param("userId") Integer userId);

    @Query(
    """
        SELECT new com.ProjectPro.ProjectPro.Dto.PendingManagerActivityReportDTO(
            
            r.id,
            r.reportDescription,
            a.id,
            a.activityName,
            a.project.name,
            a.employee.id,
            a.employee.name,
            r.submissionDate,
            CASE
                when r.grading is NULL THEN 'Pending'
                ELSE 'Reviewed'
            END AS status
        
        )
        FROM Report r
        JOIN r.activity a
        JOIN a.project p
        JOIN p.projectManager pm
        JOIN pm.user u
        WHERE u.id =:userId
    """
    )
    List<PendingManagerActivityReportDTO> findActivityReportsByTheLoggedInProjectManager(@Param("userId") int userId);


    @Query("""
           SELECT COUNT(r)
           FROM Report r
           WHERE r.taskManagement.project.directorate.headOfDirectorate.user.id = :hodId
           """)
    int countTotalReportsByHodId(int hodId);

    @Query("""
           SELECT COUNT(r)
           FROM Report r
           WHERE r.taskManagement.project.directorate.headOfDirectorate.user.id = :hodId
           AND r.taskManagement.status = 'In Progress'
           """)
    int countInProgressReportsByHodId(int hodId);

    @Query("""
           SELECT COUNT(r)
           FROM Report r
           WHERE r.taskManagement.project.directorate.headOfDirectorate.user.id = :hodId
           AND r.taskManagement.status = 'Completed'
           """)
    int countCompletedReportsByHodId(int hodId);

    @Query("""
               SELECT MONTH(r.submissionDate), COUNT(r)
               FROM Report r
               WHERE r.taskManagement.project.directorate.headOfDirectorate.user.id = :hodId
               GROUP BY MONTH(r.submissionDate)
               ORDER BY MONTH(r.submissionDate)
           """)
    List<Object[]> findMonthlyReportStatsByHodId(int hodId);


}
