package com.ProjectPro.ProjectPro.repository;

import com.ProjectPro.ProjectPro.entity.ImplementingAgency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImplementingAgencyRepo extends JpaRepository<ImplementingAgency, Integer> {
    List<ImplementingAgency> findImplementingAgenciesByDirectorateId(int theId);

}
