package com.ProjectPro.ProjectPro.dao;

import com.ProjectPro.ProjectPro.entity.ImplementingAgency;
import com.ProjectPro.ProjectPro.repository.ImplementingAgencyRepo;
import com.ProjectPro.ProjectPro.service.ImplementingAgencyService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ImplementingAgencyImpl implements ImplementingAgencyService {

    private ImplementingAgencyRepo implementingAgencyRepo;

    public ImplementingAgencyImpl(ImplementingAgencyRepo implementingAgencyRepo) {
        this.implementingAgencyRepo = implementingAgencyRepo;
    }

    @Override
    public ImplementingAgency save(ImplementingAgency implementingAgency) {
        return implementingAgencyRepo.save(implementingAgency);
    }

    @Override
    public ImplementingAgency findById(int theId) {
        Optional<ImplementingAgency> result = implementingAgencyRepo.findById(theId);
        ImplementingAgency implementingAgency = null;
        if (result.isPresent()){
            implementingAgency = result.get();
        }else {
            throw new RuntimeException("Id not Found!");
        }
        return implementingAgency;
    }

    @Override
    public void delete(int theId) {
        implementingAgencyRepo.deleteById(theId);
    }

    @Override
    public List<ImplementingAgency> findAll() {
        return implementingAgencyRepo.findAll();
    }

    @Override
    public List<ImplementingAgency> findSubObjectivesByObjectivesId(int theId) {
        return implementingAgencyRepo.findImplementingAgenciesByDirectorateId(theId);
    }

}
