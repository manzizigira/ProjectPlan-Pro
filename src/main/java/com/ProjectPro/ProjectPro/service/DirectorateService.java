package com.ProjectPro.ProjectPro.service;

import com.ProjectPro.ProjectPro.entity.Directorate;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DirectorateService {

    Directorate save(Directorate directorate);

    Directorate findById(int theId);

    void delete(int theId);

    List<Directorate> findAll();

    List<Directorate> findDirectoratesByUserId(int userId);


}
