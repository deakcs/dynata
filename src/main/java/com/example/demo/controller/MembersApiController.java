package com.example.demo.controller;

import static com.example.demo.helper.Constants.COMPLETED;

import com.example.demo.service.MembersService;
import generated.openapi.api.MembersApi;
import generated.openapi.model.ListMemberPointsResponse;
import generated.openapi.model.ListMembersOfCompletedSurveyResponse;
import generated.openapi.model.ListPotentialMembersOfSurveyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MembersApiController implements MembersApi {

    private final MembersService membersService;

    @Override
    @Transactional
    public ResponseEntity<ListMembersOfCompletedSurveyResponse> listMembersOfCompletedSurvey(Integer surveyId) {
        var response = membersService.getMembersBySurveyIdAndStatusId(surveyId, COMPLETED);

        return new ResponseEntity<>(
                new ListMembersOfCompletedSurveyResponse().members(response),
                HttpStatus.OK
        );
    }

    @Override
    @Transactional
    public ResponseEntity<ListMemberPointsResponse> listMemberPoints(Integer memberId) {
        var response = membersService.getMemberPoints(memberId);

        return new ResponseEntity<>(
                new ListMemberPointsResponse().memberPoints(response),
                HttpStatus.OK
        );
    }

    @Override
    @Transactional
    public ResponseEntity<ListPotentialMembersOfSurveyResponse> listPotentialMembersOfSurvey(Integer surveyId) {
        var response = membersService.getPotentialSurveyMembers(surveyId);

        return new ResponseEntity<>(
                new ListPotentialMembersOfSurveyResponse().members(response),
                HttpStatus.OK
        );
    }
}
