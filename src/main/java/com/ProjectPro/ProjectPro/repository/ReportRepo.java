package com.ProjectPro.ProjectPro.repository;

import com.ProjectPro.ProjectPro.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
        SELECT r.id as reportId,
            r.reportDescription as reportTitle,
            t.name as taskName,
            e.name as supervisorName,
            r.submissionDate as submissionDate,
            CASE
                when r.grading is NULL THEN 'Pending'
                ELSE 'Reviewed'
            END AS status
        From Report r
        JOIN r.taskManagement t
        JOIN r.employee e
        JOIN e.user u
        JOIN u.roles ur
        WHERE ur.roleName = 'SUPERVISOR'        
    """)
    List<Map<String,Object>> findPendingReportsBySupervisors();

    @Query("""
        SELECT r.id as reportId,
            r.reportDescription as reportTitle,
            t.name as taskName,
            t.taskLeader.name as taskLeader,
            r.submissionDate as submissionDate,
            CASE
                when r.grading is NULL THEN 'Pending'
                ELSE 'Reviewed'
            END AS status
        From Report r
        JOIN r.taskManagement t
        JOIN r.employee e
        JOIN e.user u
        JOIN u.roles ur
        WHERE ur.roleName = 'EMPLOYEE'        
    """)
    List<Map<String,Object>> findPendingReportsByTaskLeader();

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
        SELECT r.id as reportId,
            r.reportDescription as reportTitle,
            t.project.name as projectName,
            e.name as projectManager,
            r.submissionDate as submissionDate,
            CASE
                when r.grading is NULL THEN 'Pending'
                ELSE 'Reviewed'
            END AS status
        From Report r
        JOIN r.taskManagement t
        JOIN r.employee e
        JOIN e.user u
        JOIN u.roles ur
        WHERE ur.roleName = 'PROJECTMANAGER'        
    """)
    List<Map<String,Object>> findPendingReportsByProjectManagers();



}
