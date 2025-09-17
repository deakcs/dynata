package com.example.demo.service.helper;

import static com.example.demo.helper.Constants.COMPLETED;
import static com.example.demo.helper.Constants.FILTERED;
import static com.example.demo.helper.Constants.REJECTED;

import com.example.demo.model.ParticipationEntity;
import com.example.demo.model.SurveyEntity;
import generated.openapi.model.GetSurveyStatisticsDTO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public final class SurveyStatisticsCalculator {

    public static List<GetSurveyStatisticsDTO> getSurveysWithStatistics(
            List<SurveyEntity> surveys, List<ParticipationEntity> participationEntities) {

        Map<Integer, List<ParticipationEntity>> participationsBySurveyId = participationEntities.stream()
                .collect(Collectors.groupingBy(ParticipationEntity::getSurveyId));

        return surveys.stream()
                .map(survey -> {
                    List<ParticipationEntity> surveysParticipations = participationsBySurveyId
                            .getOrDefault(survey.getId(), List.of());

                    Map<Integer, Long> statusCounts = surveysParticipations.stream()
                            .collect(Collectors.groupingBy(ParticipationEntity::getStatusId, Collectors.counting()));

                    long numberOfRejectedParticipants = statusCounts.getOrDefault(REJECTED, 0L);
                    long numberOfFilteredParticipants = statusCounts.getOrDefault(FILTERED, 0L);
                    long numberOfCompletes = statusCounts.getOrDefault(COMPLETED, 0L);

                    var lengthStats = surveysParticipations.stream()
                            .map(ParticipationEntity::getLength)
                            .filter(Objects::nonNull)
                            .mapToLong(Integer::intValue)
                            .summaryStatistics();

                    return new GetSurveyStatisticsDTO()
                            .surveyId(survey.getId())
                            .surveyName(survey.getName())
                            .numberOfRejectedParticipants((int) numberOfRejectedParticipants)
                            .numberOfFilteredParticipants((int) numberOfFilteredParticipants)
                            .numberOfCompletes((int) numberOfCompletes)
                            .averageLengthOfTimeSpentOnSurvey(
                                    BigDecimal.valueOf(lengthStats.getAverage())
                                            .setScale(3, RoundingMode.HALF_UP)
                            );
                })
                .toList();
    }
}
