package com.ProjectPro.ProjectPro.repository;

import com.ProjectPro.ProjectPro.entity.Grading;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GradingRepo extends JpaRepository<Grading, Integer> {
    int countByGradeIsNotNull();
    Grading findByReportId(int reportId);

    List<Grading> findGradingsByEmployeeId(int employeeId);

    List<Grading> findByEmployeeId(int employeeId);
}
