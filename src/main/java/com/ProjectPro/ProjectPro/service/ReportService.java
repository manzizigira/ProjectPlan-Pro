package com.ProjectPro.ProjectPro.service;

import com.ProjectPro.ProjectPro.Dto.*;
import com.ProjectPro.ProjectPro.entity.Activity;
import com.ProjectPro.ProjectPro.entity.Employee;
import com.ProjectPro.ProjectPro.entity.Report;
import com.ProjectPro.ProjectPro.entity.TaskManagement;
import org.springframework.data.repository.query.Param;

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

    List<PendingManagerReportDTO> findPendingReportsByProjectManagers();

    List<Map<String,Object>> findPendingReportsByEmployees();

    List<PendingReportDTO> findPendingReportsByTaskLeader();

    List<PendingSupervisorReportDTO> findPendingReportsBySupervisors();

    List<PendingTaskLeaderReport> findReportsByEmployeesForTaskLeader(@Param("taskLeaderId") int taskLeaderId);

    Report findLatestReportByTaskAndEmployee(TaskManagement task, Employee employee);
    Report findLatestReportByActivityAndEmployee(Activity activity, Employee employee);
    Report findLatestReportByEmployee(Employee employee);

    List<Report> findReportsByTaskLeaderUserId(@Param("userId") Integer userId);
    List<PendingManagerActivityReportDTO> getActivityReportsByTheLoggedInProjectManager(int userId);

    int getTotalReportsByHod(int hodId);
    int getInProgressReportsByHod(int hodId);
    int getCompletedReportsByHod(int hodId);
    List<Object[]> getMonthlyReportStats(int hodId);



}
