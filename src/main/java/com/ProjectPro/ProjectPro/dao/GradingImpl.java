package com.ProjectPro.ProjectPro.dao;

import com.ProjectPro.ProjectPro.entity.Employee;
import com.ProjectPro.ProjectPro.entity.Grading;
import com.ProjectPro.ProjectPro.entity.Report;
import com.ProjectPro.ProjectPro.repository.EmployeeRepo;
import com.ProjectPro.ProjectPro.repository.GradingRepo;
import com.ProjectPro.ProjectPro.repository.ReportRepo;
import com.ProjectPro.ProjectPro.service.GradingService;
import com.ProjectPro.ProjectPro.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class GradingImpl implements GradingService {

    private GradingRepo gradingRepo;

    private ReportService reportService;

    private ReportRepo reportRepo;

    private EmployeeRepo employeeRepo;

    @Autowired
    public GradingImpl(GradingRepo gradingRepo, ReportService reportService, ReportRepo reportRepo, EmployeeRepo employeeRepo) {
        this.gradingRepo = gradingRepo;
        this.reportService = reportService;
        this.reportRepo = reportRepo;
        this.employeeRepo = employeeRepo;
    }

    @Override
    public Grading save(Grading grading) {
        return gradingRepo.save(grading);
    }

    @Override
    public void delete(int theId) {
        gradingRepo.deleteById(theId);
    }

    @Override
    public List<Grading> findAll() {
        return gradingRepo.findAll();
    }

    @Override
    public Grading findById(int theId) {
        Optional<Grading> result = gradingRepo.findById(theId);
        Grading grading = null;

        if (result.isPresent()){
            grading.getId();
        }else{
            throw new RuntimeException("ID not Found!");
        }
        return grading;
    }

    @Override
    public boolean gradeReport(int reportId, int employeeId, String grade, String comments, Date gradingDate) {
        try {
            // Find the report by ID
            Optional<Report> reportOptional = reportRepo.findById(reportId);
            Optional<Employee> employeeOptional = employeeRepo.findById(employeeId);

            if (!reportOptional.isPresent() || !employeeOptional.isPresent()) {
                throw new RuntimeException("Report or Employee not found");
            }

            Report report = reportOptional.get();
            Employee employee = employeeOptional.get();

            // Create or update the grading record
            Grading grading = new Grading();
            grading.setReport(report);
            grading.setEmployee(employee);
            grading.setGrade(grade);
            grading.setGradingDate(gradingDate);
            grading.setComments(comments);

            gradingRepo.save(grading);

            return true;
        } catch (Exception e) {
            // Handle exceptions, e.g., log the error
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int countGradedReports() {
        return gradingRepo.countByGradeIsNotNull();
    }

    @Override
    public Grading findGradingByReportId(int reportId) {
        return gradingRepo.findByReportId(reportId);
    }

    @Override
    public List<Grading> findGradingsByEmployeeId(int employeeId) {
        return gradingRepo.findGradingsByEmployeeId(employeeId);
    }

    @Override
    public List<Grading> findGradingsByUserId(int userId) {
        Employee employee = employeeRepo.findByUserId(userId);

        if (employee != null) {
            // Find the gradings associated with this employee
            return this.findByEmployeeId(employee.getId());
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Grading> findByEmployeeId(int employeeId) {
        return gradingRepo.findByEmployeeId(employeeId);
    }
}
