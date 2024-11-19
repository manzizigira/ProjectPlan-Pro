package com.ProjectPro.ProjectPro.service;

import com.ProjectPro.ProjectPro.entity.ImplementingAgency;

import java.util.List;

public interface ImplementingAgencyService {

    ImplementingAgency save(ImplementingAgency implementingAgency);

    ImplementingAgency findById(int theId);

    void delete(int theId);

    List<ImplementingAgency> findAll();

    List<ImplementingAgency> findSubObjectivesByObjectivesId(int theId);


}
