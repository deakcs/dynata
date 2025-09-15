package com.example.demo.controller;

import static com.example.demo.helper.Constants.COMPLETED;

import com.example.demo.service.SurveysService;
import generated.openapi.api.SurveysApi;
import generated.openapi.model.ListCompletedSurveysResponse;
import generated.openapi.model.ListSurveysStatisticsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SurveysApiController implements SurveysApi {

    private final SurveysService surveysService;

    @Override
    @Transactional
    public ResponseEntity<ListCompletedSurveysResponse> listCompletedSurveysOfMember(Integer memberId) {
        var response = surveysService.getSurveysByMemberIdAndStatusId(memberId, COMPLETED);

        return new ResponseEntity<>(
                new ListCompletedSurveysResponse().surveys(response),
                HttpStatus.OK
        );
    }

    @Override
    @Transactional
    public ResponseEntity<ListSurveysStatisticsResponse> listSurveysStatistics() {
        var response = surveysService.listSurveysStatistics();

        return new ResponseEntity<>(
                new ListSurveysStatisticsResponse().surveys(response),
                HttpStatus.OK
        );
    }
}
