package com.example.demo.service;

import com.example.demo.model.ParticipationEntity;
import com.example.demo.repository.ParticipationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class ParticipationService {

    private final ParticipationRepository participationRepository;

    List<Integer> getMemberIdsBySurveyIdAndStatusId(int surveyId, int statusId) {
        return participationRepository.findAllBySurveyIdAndStatusId(surveyId, statusId)
                .stream()
                .map(ParticipationEntity::getMemberId)
                .toList();
    }

    List<Integer> getSurveyIdsByMemberIdAndStatusId(int memberId, int statusId) {
        return participationRepository.findAllByMemberIdAndStatusId(memberId, statusId)
                .stream()
                .map(ParticipationEntity::getSurveyId)
                .toList();
    }

    List<ParticipationEntity> getBySurveyIds(List<Integer> surveyIds) {
        return participationRepository.findAllBySurveyIdIn(surveyIds);
    }
}