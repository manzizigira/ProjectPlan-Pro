package com.ProjectPro.ProjectPro.dao;


import com.ProjectPro.ProjectPro.Dto.PendingManagerActivityReportDTO;
import com.ProjectPro.ProjectPro.Dto.PendingManagerReportDTO;
import com.ProjectPro.ProjectPro.Dto.PendingReportDTO;
import com.ProjectPro.ProjectPro.Dto.PendingSupervisorReportDTO;
import com.ProjectPro.ProjectPro.entity.Activity;
import com.ProjectPro.ProjectPro.entity.Employee;
import com.ProjectPro.ProjectPro.entity.Report;
import com.ProjectPro.ProjectPro.entity.TaskManagement;
import com.ProjectPro.ProjectPro.repository.GradingRepo;
import com.ProjectPro.ProjectPro.repository.ReportRepo;
import com.ProjectPro.ProjectPro.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ReportImpl implements ReportService {

    private ReportRepo reportRepo;

    private GradingRepo gradingRepo;

    @Autowired
    public ReportImpl(ReportRepo reportRepo, GradingRepo gradingRepo) {
        this.reportRepo = reportRepo;
        this.gradingRepo = gradingRepo;
    }

    @Override
    public Report save(Report report) {
        return reportRepo.save(report);
    }

    @Override
    public void delete(int theId) {
        reportRepo.deleteById(theId);
    }

    @Override
    public List<Report> findAll() {
        return reportRepo.findAll();
    }

    @Override
    public Report findById(int theId) {
        Optional<Report> report = reportRepo.findById(theId);

        Report report1 = null;

        if (report.isPresent()) {
            report1 = report.get();
        } else {
            throw new RuntimeException("ID not Found!");
        }
        return report1;
    }

    @Override
    public int countPendingReports() {
        return reportRepo.countPendingReports();
    }

    @Override
    public List<Integer> findPendingReportIds() {
        return reportRepo.findPendingReportIds();
    }

    @Override
    public List<Report> findReportsByEmployeeId(int employeeId) {
        return reportRepo.findAllByEmployeeId(employeeId);
    }

    @Override
    public int countGradedReports() {
        return reportRepo.countGradedReports();
    }

    @Override
    public List<PendingManagerReportDTO> findPendingReportsByProjectManagers() {
        return reportRepo.findPendingReportsByProjectManagers();
    }

    @Override
    public List<Map<String, Object>> findPendingReportsByEmployees() {
        return reportRepo.findPendingReportsByEmployees();
    }

    @Override
    public List<PendingReportDTO> findPendingReportsByTaskLeader() {
        return reportRepo.findPendingReportsByTaskLeader();
    }

    @Override
    public List<PendingSupervisorReportDTO> findPendingReportsBySupervisors() {
        return reportRepo.findPendingReportsBySupervisors();
    }

    @Override
    public List<Report> findReportsByEmployeesForTaskLeader(int taskLeaderId) {
        return reportRepo.findReportsByEmployeesForTaskLeader(taskLeaderId);
    }

    @Override
    public Report findLatestReportByTaskAndEmployee(TaskManagement task, Employee employee) {
        return reportRepo.findTopByTaskManagementAndEmployeeOrderBySubmissionDateDesc(task,employee);
    }

    @Override
    public Report findLatestReportByActivityAndEmployee(Activity activity, Employee employee) {
        return reportRepo.findTopByActivityAndEmployeeOrderBySubmissionDateDesc(activity, employee);
    }

    @Override
    public Report findLatestReportByEmployee(Employee employee) {
        return reportRepo.findTopByEmployeeOrderBySubmissionDateDesc(employee);
    }

    @Override
    public List<Report> findReportsByTaskLeaderUserId(Integer userId) {
        return reportRepo.findReportsByTaskLeaderUserId(userId);
    }

    @Override
    public List<PendingManagerActivityReportDTO> getActivityReportsByTheLoggedInProjectManager(int userId) {
        return reportRepo.findActivityReportsByTheLoggedInProjectManager(userId);
    }


}
