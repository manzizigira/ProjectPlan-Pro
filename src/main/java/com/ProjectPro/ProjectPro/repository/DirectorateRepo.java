package com.ProjectPro.ProjectPro.repository;

import com.ProjectPro.ProjectPro.entity.Directorate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DirectorateRepo extends JpaRepository<Directorate, Integer> {
    @Query(
            """
                SELECT d
                FROM Directorate d
                JOIN d.headOfDirectorate e
                JOIN e.user u
                WHERE u.id =:userId
            """
    )
    List<Directorate> findDirectoratesByUserId(@Param("userId") int userId);
}
