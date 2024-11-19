package com.ProjectPro.ProjectPro.service;

import com.ProjectPro.ProjectPro.entity.Grading;

import java.util.Date;
import java.util.List;

public interface GradingService {

    Grading save(Grading grading);

    void delete(int theId);

    List<Grading> findAll();

    Grading findById(int theId);

    public boolean gradeReport(int reportId, int employeeId, String grade, String comments, Date gradingDate);

    public int countGradedReports();

    public Grading findGradingByReportId(int reportId);

    List<Grading> findGradingsByEmployeeId(int employeeId);

    public List<Grading> findGradingsByUserId(int userId);

    List<Grading> findByEmployeeId(int employeeId);
}
