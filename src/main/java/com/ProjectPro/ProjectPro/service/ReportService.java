package com.ProjectPro.ProjectPro.service;

import com.ProjectPro.ProjectPro.entity.Report;

import java.util.List;
import java.util.Map;

public interface ReportService {

    Report save(Report report);

    void delete(int theId);

    List<Report> findAll();

    Report findById(int theId);

    public int countPendingReports();

    public List<Integer> findPendingReportIds();

    public List<Report> findReportsByEmployeeId(int employeeId);

    public int countGradedReports();

    List<Map<String,Object>> findPendingReportsByProjectManagers();

    List<Map<String,Object>> findPendingReportsByEmployees();

    List<Map<String,Object>> findPendingReportsByTaskLeader();

    List<Map<String,Object>> findPendingReportsBySupervisors();


}
