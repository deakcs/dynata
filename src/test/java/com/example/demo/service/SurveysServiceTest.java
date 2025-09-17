package com.example.demo.service;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.example.demo.exception.NotFoundException;
import com.example.demo.mapper.SurveyMapperImpl;
import com.example.demo.model.ParticipationEntity;
import com.example.demo.model.SurveyEntity;
import com.example.demo.repository.SurveyRepository;
import generated.openapi.model.GetSurveyDTO;
import generated.openapi.model.GetSurveyStatisticsDTO;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SurveysServiceTest {

    @InjectMocks
    private SurveysService surveysService;

    @Mock
    private SurveyRepository surveyRepository;

    @Mock
    private ParticipationService participationService;

    @Spy
    private SurveyMapperImpl surveyMapper;

    @Test
    void getSurveysByMemberIdAndStatusId() {
        // given
        var memberId = 1;
        var statusId = 2;

        when(participationService.getSurveyIdsByMemberIdAndStatusId(memberId, statusId))
                .thenReturn(List.of(11, 22));
        when(surveyRepository.findAllById(List.of(11, 22)))
                .thenReturn(List.of(
                        new SurveyEntity(1, "name1", 2, 3, 4, null),
                        new SurveyEntity(11, "name11", 22, 33, 44, null)
                ));

        // when
        var result = surveysService.getSurveysByMemberIdAndStatusId(memberId, statusId);

        // then
        var expected = List.of(
                new GetSurveyDTO().id(1).name("name1").expectedCompletes(2).expectedPoints(3).filteredPoints(4),
                new GetSurveyDTO().id(11).name("name11").expectedCompletes(22).expectedPoints(33).filteredPoints(44)
        );
        assertEquals(expected, result);

        verify(surveyMapper).toGetSurveyDTOs(List.of(
                new SurveyEntity(1, "name1", 2, 3, 4, null),
                new SurveyEntity(11, "name11", 22, 33, 44, null)
        ));

        verifyNoMoreInteractions(participationService);
        verifyNoMoreInteractions(surveyRepository);
    }

    @Test
    void getSurveysByMemberIdAndStatusId_no_participation() {
        // given
        var memberId = 1;
        var statusId = 2;

        when(participationService.getSurveyIdsByMemberIdAndStatusId(memberId, statusId))
                .thenReturn(emptyList());
        when(surveyRepository.findAllById(emptyList()))
                .thenReturn(emptyList());

        // when
        var result = surveysService.getSurveysByMemberIdAndStatusId(memberId, statusId);

        // then
        var expected = emptyList();
        assertEquals(expected, result);

        verify(surveyMapper).toGetSurveyDTOs(emptyList());

        verifyNoMoreInteractions(participationService);
        verifyNoMoreInteractions(surveyRepository);
        verifyNoMoreInteractions(surveyMapper);
    }

    @Test
    void getSurveysByMemberIdAndStatusId_no_survey() {
        // given
        var memberId = 1;
        var statusId = 2;

        when(participationService.getSurveyIdsByMemberIdAndStatusId(memberId, statusId))
                .thenReturn(List.of(11, 22));
        when(surveyRepository.findAllById(List.of(11, 22)))
                .thenReturn(emptyList());

        // when
        var result = surveysService.getSurveysByMemberIdAndStatusId(memberId, statusId);

        // then
        var expected = emptyList();
        assertEquals(expected, result);

        verify(surveyMapper).toGetSurveyDTOs(emptyList());

        verifyNoMoreInteractions(participationService);
        verifyNoMoreInteractions(surveyRepository);
        verifyNoMoreInteractions(surveyMapper);
    }

    @Test
    void getSurveys() {
        // given
        var surveyIds = List.of(1, 11);

        when(surveyRepository.findAllById(surveyIds))
                .thenReturn(List.of(
                        new SurveyEntity(1, "name1", 2, 3, 4, null),
                        new SurveyEntity(11, "name11", 22, 33, 44, null)
                ));

        // when
        var result = surveysService.getSurveys(surveyIds);

        // then
        var expected = List.of(
                new GetSurveyDTO().id(1).name("name1").expectedCompletes(2).expectedPoints(3).filteredPoints(4),
                new GetSurveyDTO().id(11).name("name11").expectedCompletes(22).expectedPoints(33).filteredPoints(44)
        );
        assertEquals(expected, result);

        verifyNoInteractions(participationService);
    }

    @Test
    void getSurveys_empty() {
        // given
        var surveyIds = List.of(1, 11);

        when(surveyRepository.findAllById(surveyIds))
                .thenReturn(emptyList());

        // when
        var result = surveysService.getSurveys(surveyIds);

        // then
        var expected = emptyList();
        assertEquals(expected, result);

        verifyNoInteractions(participationService);
    }

    @Test
    void getPotentialSurveyMemberIds() {
        // given
        var surveyId = 1;

        var participationEntities = List.of(
                new ParticipationEntity(1, 21, 31, 1, 5),
                new ParticipationEntity(2, 22, 32, 2, 5),
                new ParticipationEntity(3, 23, 33, 3, 5),
                new ParticipationEntity(4, 24, 34, 4, 5)
        );
        when(surveyRepository.findById(surveyId))
                .thenReturn(Optional.of(
                        new SurveyEntity(1, "name1", 2, 3, 4, participationEntities)
                ));

        // when
        var result = surveysService.getPotentialSurveyMemberIds(surveyId);

        // then
        var expected = List.of(21, 22);
        assertEquals(expected, result);
    }

    @Test
    void getPotentialSurveyMemberIds_filtered_completed() {
        // given
        var surveyId = 1;

        var participationEntities = List.of(
                new ParticipationEntity(1, 21, 31, 3, 5),
                new ParticipationEntity(2, 22, 32, 4, 5),
                new ParticipationEntity(3, 23, 33, 3, 5),
                new ParticipationEntity(4, 24, 34, 4, 5)
        );
        when(surveyRepository.findById(surveyId))
                .thenReturn(Optional.of(
                        new SurveyEntity(1, "name1", 2, 3, 4, participationEntities)
                ));

        // when
        var result = surveysService.getPotentialSurveyMemberIds(surveyId);

        // then
        var expected = emptyList();
        assertEquals(expected, result);
    }

    @Test
    void getPotentialSurveyMemberIds_noSurvey() {
        // given
        var surveyId = 1;

        when(surveyRepository.findById(surveyId))
                .thenReturn(Optional.empty());

        // when
        var result = assertThrows(NotFoundException.class, () -> surveysService.getPotentialSurveyMemberIds(surveyId));

        // then
        var expected = new NotFoundException(
                "Could not find record.",
                SurveyEntity.class.getSimpleName(),
                "id",
                String.valueOf(surveyId)
        );
        assertEquals(expected.getMessage(), result.getMessage());
        assertEquals(expected.getEntity(), result.getEntity());
        assertEquals(expected.getProperty(), result.getProperty());
        assertEquals(expected.getInvalidValue(), result.getInvalidValue());
    }

    @Test
    void listSurveysStatistics() {
        // given
        var participationEntities = List.of(
                new ParticipationEntity(0, 21, 2, 1, null),
                new ParticipationEntity(1, 21, 1, 1, null),
                new ParticipationEntity(2, 22, 1, 2, null),
                new ParticipationEntity(4, 23, 1, 3, null),
                new ParticipationEntity(5, 24, 1, 3, null),
                new ParticipationEntity(6, 25, 1, 4, 22),
                new ParticipationEntity(7, 26, 1, 4, 34),
                new ParticipationEntity(8, 27, 1, 4, 15),
                new ParticipationEntity(9, 28, 1, 4, 24),
                new ParticipationEntity(10, 29, 1, 4, 15),
                new ParticipationEntity(11, 30, 1, 4, 9)
        );
        when(surveyRepository.findAll())
                .thenReturn(List.of(
                        new SurveyEntity(1, "name1", 2, 3, 4, null)
                ));
        when(participationService.getBySurveyIds(List.of(1)))
                .thenReturn(participationEntities);

        // when
        var result = surveysService.listSurveysStatistics();

        // then
        var expected = List.of(new GetSurveyStatisticsDTO()
                .surveyId(1)
                .surveyName("name1")
                .numberOfCompletes(6)
                .numberOfFilteredParticipants(2)
                .numberOfRejectedParticipants(1)
                .averageLengthOfTimeSpentOnSurvey(new BigDecimal("19.833")));
        assertEquals(expected, result);
    }
}