package com.example.demo.repository;

import com.example.demo.model.SurveyEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyRepository extends JpaRepository<SurveyEntity, Integer> {

    List<SurveyEntity> findAllByIdIn(List<Integer> statusIds);
}
