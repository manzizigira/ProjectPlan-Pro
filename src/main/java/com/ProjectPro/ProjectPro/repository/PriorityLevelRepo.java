package com.ProjectPro.ProjectPro.repository;

import com.ProjectPro.ProjectPro.entity.PriorityLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface PriorityLevelRepo extends JpaRepository<PriorityLevel, Integer> {
    @Query("SELECT p.calculatePriorityValue(:param1, :param2, :param3) FROM PriorityLevel p WHERE p.id = :id")
    int calculatePriorityValue(@Param("id") int id, @Param("param1") int param1, @Param("param2") int param2, @Param("param3") int param3);

    PriorityLevel findByProjectId(int projectId);
    PriorityLevel findByTaskManagementId(int taskId);
    PriorityLevel findByActivityId(int activityId);

    int deleteByProjectId(int projectId);
    int deleteByTaskManagementId(int projectId);
    int deleteByActivityId(int projectId);
}
