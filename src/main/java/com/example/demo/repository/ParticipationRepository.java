package com.example.demo.repository;

import com.example.demo.model.ParticipationEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipationRepository extends JpaRepository<ParticipationEntity, Integer> {

    List<ParticipationEntity> findAllBySurveyIdAndStatusId(Integer surveyId, Integer statusId);

    List<ParticipationEntity> findAllByMemberIdAndStatusId(Integer memberId, Integer statusId);

    List<ParticipationEntity> findAllBySurveyIdIn(List<Integer> surveyIds);
}
