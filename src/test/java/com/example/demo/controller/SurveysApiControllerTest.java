package com.example.demo.controller;

import static com.example.demo.helper.Constants.COMPLETED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.example.demo.service.SurveysService;
import generated.openapi.model.GetSurveyDTO;
import generated.openapi.model.GetSurveyStatisticsDTO;
import generated.openapi.model.ListCompletedSurveysResponse;
import generated.openapi.model.ListSurveysStatisticsResponse;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class SurveysApiControllerTest {

    @InjectMocks
    private SurveysApiController surveysApiController;

    @Mock
    private SurveysService surveysService;

    @Test
    void listCompletedSurveys() {
        // given
        var memberId = 1;

        when(surveysService.getSurveysByMemberIdAndStatusId(memberId, COMPLETED))
                .thenReturn(List.of(new GetSurveyDTO()));

        // when
        var result = surveysApiController.listCompletedSurveysOfMember(memberId);

        // then
        var expected = new ResponseEntity<>(
                new ListCompletedSurveysResponse().surveys(List.of(new GetSurveyDTO())),
                HttpStatus.OK
        );
        assertEquals(expected, result);
    }

    @Test
    void listSurveysStatistics() {
        // given
        when(surveysService.listSurveysStatistics())
                .thenReturn(List.of(new GetSurveyStatisticsDTO()));

        // when
        var result = surveysApiController.listSurveysStatistics();

        // then
        var expected = new ResponseEntity<>(
                new ListSurveysStatisticsResponse().surveys(List.of(new GetSurveyStatisticsDTO())),
                HttpStatus.OK
        );
        assertEquals(expected, result);
    }
}