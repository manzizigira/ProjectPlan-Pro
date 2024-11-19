package com.ProjectPro.ProjectPro.repository;

import com.ProjectPro.ProjectPro.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

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



}
