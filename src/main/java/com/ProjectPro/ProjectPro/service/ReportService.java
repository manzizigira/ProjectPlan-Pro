package com.ProjectPro.ProjectPro.service;

import com.ProjectPro.ProjectPro.entity.Report;

import java.util.Date;
import java.util.List;

public interface ReportService {

    Report save(Report report);

    void delete(int theId);

    List<Report> findAll();

    Report findById(int theId);

    public int countPendingReports();

    public List<Integer> findPendingReportIds();

    public List<Report> findReportsByEmployeeId(int employeeId);

    public int countGradedReports();


}
