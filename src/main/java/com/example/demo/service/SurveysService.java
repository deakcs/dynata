package com.example.demo.service;

import static com.example.demo.helper.Constants.NOT_ASKED;
import static com.example.demo.helper.Constants.REJECTED;

import com.example.demo.exception.NotFoundException;
import com.example.demo.mapper.SurveyMapper;
import com.example.demo.model.ParticipationEntity;
import com.example.demo.model.SurveyEntity;
import com.example.demo.repository.SurveyRepository;
import com.example.demo.service.helper.SurveyStatisticsCalculator;
import generated.openapi.model.GetSurveyDTO;
import generated.openapi.model.GetSurveyStatisticsDTO;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SurveysService {

    private final SurveyRepository surveyRepository;
    private final ParticipationService participationService;
    private final SurveyMapper surveyMapper;

    public List<GetSurveyDTO> getSurveysByMemberIdAndStatusId(int memberId, int statusId) {
        var surveyIds = participationService.getSurveyIdsByMemberIdAndStatusId(memberId, statusId);
        var surveyEntities = surveyRepository.findAllById(surveyIds);

        return surveyMapper.toGetSurveyDTOs(surveyEntities);
    }

    public List<GetSurveyStatisticsDTO> listSurveysStatistics() {
        var surveys = surveyRepository.findAll();

        var surveyIds = surveys.stream()
                .map(SurveyEntity::getId)
                .toList();
        var participationEntities = participationService.getBySurveyIds(surveyIds);

        return SurveyStatisticsCalculator.getSurveysWithStatistics(surveys, participationEntities);
    }

    List<GetSurveyDTO> getSurveys(List<Integer> surveyIds) {
        var surveyEntities = surveyRepository.findAllById(surveyIds);

        return surveyMapper.toGetSurveyDTOs(surveyEntities);
    }

    List<Integer> getPotentialSurveyMemberIds(int surveyId) {
        return surveyRepository.findById(surveyId)
                .orElseThrow(() -> new NotFoundException(
                        "Could not find record.",
                        SurveyEntity.class.getSimpleName(),
                        "id",
                        String.valueOf(surveyId))
                ).getParticipationEntities()
                .stream()
                .filter(p -> NOT_ASKED == p.getStatusId() || REJECTED == p.getStatusId())
                .map(ParticipationEntity::getMemberId)
                .collect(Collectors.toList());
    }
}
