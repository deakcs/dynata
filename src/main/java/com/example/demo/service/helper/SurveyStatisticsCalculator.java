package com.example.demo.service.helper;

import static com.example.demo.helper.Constants.COMPLETED;
import static com.example.demo.helper.Constants.FILTERED;
import static com.example.demo.helper.Constants.REJECTED;

import com.example.demo.model.ParticipationEntity;
import com.example.demo.model.SurveyEntity;
import generated.openapi.model.GetSurveyStatisticsDTO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class SurveyStatisticsCalculator {

    public static List<GetSurveyStatisticsDTO> getSurveysWithStatistics(
            List<SurveyEntity> surveys, List<ParticipationEntity> participationEntities) {

        List<GetSurveyStatisticsDTO> getSurveyStatisticsDTOs = new ArrayList<>();

        for (SurveyEntity surveyEntity : surveys) {
            int numberOfRejectedParticipants = 0;
            int umberOfFilteredParticipants = 0;
            int numberOfCompletes = 0;
            long averageLengthOfTimeSpentOnSurvey = 0;
            int participationSize = 0;

            for (ParticipationEntity participationEntity : participationEntities) {
                if (surveyEntity.getId().equals(participationEntity.getSurveyId())) {
                    switch (participationEntity.getStatusId()) {
                        case REJECTED -> numberOfRejectedParticipants++;
                        case FILTERED -> umberOfFilteredParticipants++;
                        case COMPLETED -> numberOfCompletes++;
                    }

                    var length = participationEntity.getLength();
                    averageLengthOfTimeSpentOnSurvey += length == null ? 0 : length;
                    participationSize++;
                }
            }

            var getSurveyStatisticsDTO = new GetSurveyStatisticsDTO();
            getSurveyStatisticsDTO.setSurveyId(surveyEntity.getId());
            getSurveyStatisticsDTO.setSurveyName(surveyEntity.getName());
            getSurveyStatisticsDTO.setNumberOfRejectedParticipants(numberOfRejectedParticipants);
            getSurveyStatisticsDTO.numberOfFilteredParticipants(umberOfFilteredParticipants);
            getSurveyStatisticsDTO.setNumberOfCompletes(numberOfCompletes);
            getSurveyStatisticsDTO.setAverageLengthOfTimeSpentOnSurvey(
                    new BigDecimal(Double.toString(averageLengthOfTimeSpentOnSurvey))
                            .divide(new BigDecimal(Double.toString(participationSize)),
                                    3, RoundingMode.HALF_UP));

            getSurveyStatisticsDTOs.add(getSurveyStatisticsDTO);
        }

        return getSurveyStatisticsDTOs;
    }
}
