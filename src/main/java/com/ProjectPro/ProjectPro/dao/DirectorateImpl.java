package com.ProjectPro.ProjectPro.dao;

import com.ProjectPro.ProjectPro.entity.Directorate;
import com.ProjectPro.ProjectPro.repository.DirectorateRepo;
import com.ProjectPro.ProjectPro.service.DirectorateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DirectorateImpl implements DirectorateService {

    private DirectorateRepo directorateRepo;

    @Autowired
    public DirectorateImpl(DirectorateRepo directorateRepo) {
        this.directorateRepo = directorateRepo;
    }

    @Override
    public Directorate save(Directorate directorate) {
        return directorateRepo.save(directorate);
    }

    @Override
    public Directorate findById(int theId) {
        Optional<Directorate> result = directorateRepo.findById(theId);

        Directorate directorate = null;

        if (result.isPresent()){
            directorate = result.get();
        }else{
            throw new RuntimeException("Id not found!");
        }

        return directorate;
    }

    @Override
    public void delete(int theId) {
        directorateRepo.deleteById(theId);
    }

    @Override
    public List<Directorate> findAll() {
        return directorateRepo.findAll();
    }

    @Override
    public List<Directorate> findDirectoratesByUserId(int userId) {
        return directorateRepo.findDirectoratesByUserId(userId);
    }

}
