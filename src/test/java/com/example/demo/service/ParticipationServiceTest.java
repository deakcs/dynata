package com.example.demo.service;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.example.demo.model.ParticipationEntity;
import com.example.demo.repository.ParticipationRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ParticipationServiceTest {

    @InjectMocks
    private ParticipationService participationService;

    @Mock
    private ParticipationRepository participationRepository;

    @Test
    void getMemberIdsBySurveyIdAndStatusId() {
        // given
        var surveyId = 1;
        var statusId = 2;

        when(participationRepository.findAllBySurveyIdAndStatusId(surveyId, statusId))
                .thenReturn(List.of(
                        new ParticipationEntity(1, 2, 3, 4, 5),
                        new ParticipationEntity(11, 22, 3, 4, 55)));

        // when
        var result = participationService.getMemberIdsBySurveyIdAndStatusId(surveyId, statusId);

        // then
        var expected = List.of(2, 22);
        assertEquals(expected, result);
    }

    @Test
    void getMemberIdsBySurveyIdAndStatusId_empty() {
        // given
        var surveyId = 1;
        var statusId = 2;

        when(participationRepository.findAllBySurveyIdAndStatusId(surveyId, statusId))
                .thenReturn(emptyList());

        // when
        var result = participationService.getMemberIdsBySurveyIdAndStatusId(surveyId, statusId);

        // then
        var expected = emptyList();
        assertEquals(expected, result);
    }

    @Test
    void getSurveyIdsByMemberIddAndStatusId() {
        // given
        var memberId = 1;
        var statusId = 2;

        when(participationRepository.findAllByMemberIdAndStatusId(memberId, statusId))
                .thenReturn(List.of(
                        new ParticipationEntity(1, 2, 3, 4, 5),
                        new ParticipationEntity(11, 2, 33, 4, 55)));

        // when
        var result = participationService.getSurveyIdsByMemberIdAndStatusId(memberId, statusId);

        // then
        var expected = List.of(3, 33);
        assertEquals(expected, result);
    }

    @Test
    void getSurveyIdsByMemberIddAndStatusId_empty() {
        // given
        var memberId = 1;
        var statusId = 2;

        when(participationRepository.findAllByMemberIdAndStatusId(memberId, statusId))
                .thenReturn(emptyList());

        // when
        var result = participationService.getSurveyIdsByMemberIdAndStatusId(memberId, statusId);

        // then
        var expected = emptyList();
        assertEquals(expected, result);
    }

    @Test
    void getBySurveyIds() {
        // given
        var surveyIds = List.of(1, 2);

        when(participationRepository.findAllBySurveyIdIn(surveyIds))
                .thenReturn(List.of(
                        new ParticipationEntity(1, 2, 1, 4, 5),
                        new ParticipationEntity(11, 2, 2, 4, 55)));

        // when
        var result = participationService.getBySurveyIds(surveyIds);

        // then
        var expected = List.of(
                new ParticipationEntity(1, 2, 1, 4, 5),
                new ParticipationEntity(11, 2, 2, 4, 55));
        assertEquals(expected, result);
    }
}