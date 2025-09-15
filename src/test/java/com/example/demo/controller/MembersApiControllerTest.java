package com.example.demo.controller;

import static com.example.demo.helper.Constants.COMPLETED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.example.demo.service.MembersService;
import generated.openapi.model.GetMemberDTO;
import generated.openapi.model.GetMemberPointsDTO;
import generated.openapi.model.ListMemberPointsResponse;
import generated.openapi.model.ListMembersOfCompletedSurveyResponse;
import generated.openapi.model.ListPotentialMembersOfSurveyResponse;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class MembersApiControllerTest {

    @InjectMocks
    private MembersApiController membersApiController;

    @Mock
    private MembersService membersService;

    @Test
    void listMembersOfCompletedSurvey() {
        // given
        var surveyId = 1;

        when(membersService.getMembersBySurveyIdAndStatusId(surveyId, COMPLETED))
                .thenReturn(List.of(new GetMemberDTO()));

        // when
        var result = membersApiController.listMembersOfCompletedSurvey(surveyId);

        // then
        var expected = new ResponseEntity<>(
                new ListMembersOfCompletedSurveyResponse().members(List.of(new GetMemberDTO())),
                HttpStatus.OK
        );
        assertEquals(expected, result);
    }

    @Test
    void listMemberPoints() {
        // given
        var memberId = 1;

        when(membersService.getMemberPoints(memberId))
                .thenReturn(List.of(new GetMemberPointsDTO()));

        // when
        var result = membersApiController.listMemberPoints(memberId);

        // then
        var expected = new ResponseEntity<>(
                new ListMemberPointsResponse().memberPoints(List.of(new GetMemberPointsDTO())),
                HttpStatus.OK
        );
        assertEquals(expected, result);
    }

    @Test
    void listPotentialMembersOfSurvey() {
        // given
        var surveyId = 1;

        when(membersService.getPotentialSurveyMembers(surveyId))
                .thenReturn(List.of(new GetMemberDTO()));

        // when
        var result = membersApiController.listPotentialMembersOfSurvey(surveyId);

        // then
        var expected = new ResponseEntity<>(
                new ListPotentialMembersOfSurveyResponse().members(List.of(new GetMemberDTO())),
                HttpStatus.OK
        );
        assertEquals(expected, result);
    }
}