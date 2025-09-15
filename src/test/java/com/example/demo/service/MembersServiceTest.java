package com.example.demo.service;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.example.demo.exception.NotFoundException;
import com.example.demo.mapper.MemberMapperImpl;
import com.example.demo.model.MemberEntity;
import com.example.demo.model.ParticipationEntity;
import com.example.demo.repository.MemberRepository;
import generated.openapi.model.GetMemberDTO;
import generated.openapi.model.GetMemberPointsDTO;
import generated.openapi.model.GetSurveyDTO;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MembersServiceTest {

    @InjectMocks
    private MembersService membersService;

    @Mock
    private MemberRepository memberRepository;

    @Spy
    private MemberMapperImpl memberMapper;

    @Mock
    private ParticipationService participationService;

    @Mock
    private SurveysService surveysService;

    @Test
    void getMembersBySurveyIdAndStatusId() {
        // given
        var surveyId = 1;
        var statusId = 2;

        when(participationService.getMemberIdsBySurveyIdAndStatusId(surveyId, statusId))
                .thenReturn(List.of(1, 11));
        when(memberRepository.findAllById(List.of(1, 11)))
                .thenReturn(List.of(
                        new MemberEntity(1, "fullName1", "email1", 1, null),
                        new MemberEntity(11, "fullName11", "email11", 0, null)
                ));

        // when
        var result = membersService.getMembersBySurveyIdAndStatusId(surveyId, statusId);

        // then
        var expected = List.of(
                new GetMemberDTO().id(1).fullName("fullName1").emailAddress("email1").isActive(1),
                new GetMemberDTO().id(11).fullName("fullName11").emailAddress("email11").isActive(0)
        );
        assertEquals(expected, result);

        verify(memberMapper).toGetMemberDTOs(List.of(
                new MemberEntity(1, "fullName1", "email1", 1, null),
                new MemberEntity(11, "fullName11", "email11", 0, null)
        ));

        verifyNoMoreInteractions(participationService);
        verifyNoMoreInteractions(memberRepository);

        verifyNoInteractions(surveysService);
    }

    @Test
    void getMembersBySurveyIdAndStatusId_no_participation() {
        // given
        var surveyId = 1;
        var statusId = 2;

        when(participationService.getMemberIdsBySurveyIdAndStatusId(surveyId, statusId))
                .thenReturn(emptyList());
        when(memberRepository.findAllById(emptyList()))
                .thenReturn(emptyList());

        // when
        var result = membersService.getMembersBySurveyIdAndStatusId(surveyId, statusId);

        // then
        var expected = emptyList();
        assertEquals(expected, result);

        verify(memberMapper).toGetMemberDTOs(emptyList());

        verifyNoMoreInteractions(participationService);
        verifyNoMoreInteractions(memberRepository);
        verifyNoMoreInteractions(memberMapper);

        verifyNoInteractions(surveysService);
    }

    @Test
    void getMembersBySurveyIdAndStatusId_no_survey() {
        // given
        var surveyId = 1;
        var statusId = 2;

        when(participationService.getMemberIdsBySurveyIdAndStatusId(surveyId, statusId))
                .thenReturn(List.of(11, 22));
        when(memberRepository.findAllById(List.of(11, 22)))
                .thenReturn(emptyList());

        // when
        var result = membersService.getMembersBySurveyIdAndStatusId(surveyId, statusId);

        // then
        var expected = emptyList();
        assertEquals(expected, result);

        verify(memberMapper).toGetMemberDTOs(emptyList());

        verifyNoMoreInteractions(participationService);
        verifyNoMoreInteractions(memberRepository);
        verifyNoMoreInteractions(memberMapper);

        verifyNoInteractions(surveysService);
    }

    @Test
    void getMemberPoints() {
        // given
        var memberId = 24;

        var participationEntities = List.of(
                new ParticipationEntity(1, 24, 31, 1, 5),
                new ParticipationEntity(2, 24, 32, 2, 5),
                new ParticipationEntity(3, 24, 33, 3, 5),
                new ParticipationEntity(4, 24, 34, 4, 5)
        );
        when(memberRepository.findById(memberId))
                .thenReturn(Optional.of(
                        new MemberEntity(24, "fullName1", "email1", 1, participationEntities)
                ));
        when(surveysService.getSurveys(List.of(33, 34)))
                .thenReturn(List.of(
                        new GetSurveyDTO()
                                .id(33)
                                .name("name33")
                                .expectedCompletes(2)
                                .expectedPoints(3)
                                .filteredPoints(4),
                        new GetSurveyDTO()
                                .id(34)
                                .name("name34")
                                .expectedCompletes(22)
                                .expectedPoints(33)
                                .filteredPoints(44)
                ));

        // when
        var result = membersService.getMemberPoints(memberId);

        // then
        var expected = List.of(
                new GetMemberPointsDTO().surveyId(33).collectedPoints(2),
                new GetMemberPointsDTO().surveyId(34).collectedPoints(22)
        );
        assertEquals(expected, result);

        verifyNoMoreInteractions(memberRepository);
        verifyNoMoreInteractions(participationService);
        verifyNoMoreInteractions(memberRepository);
        verifyNoMoreInteractions(surveysService);
    }

    @Test
    void getMemberPoints_no_member_exception() {
        // given
        var memberId = 1;

        when(memberRepository.findById(memberId))
                .thenReturn(Optional.empty());

        // when
        var result = assertThrows(NotFoundException.class, () -> membersService.getMemberPoints(memberId));

        // then
        var expected = new NotFoundException(
                "Could not find record.",
                MemberEntity.class.getSimpleName(),
                "id",
                String.valueOf(memberId)
        );
        assertEquals(expected.getMessage(), result.getMessage());
        assertEquals(expected.getEntity(), result.getEntity());
        assertEquals(expected.getProperty(), result.getProperty());
        assertEquals(expected.getInvalidValue(), result.getInvalidValue());

        verifyNoMoreInteractions(memberRepository);

        verifyNoInteractions(participationService);
        verifyNoInteractions(surveysService);
    }

    @Test
    void getPotentialSurveyMembers() {
        // given
        var surveyId = 1;

        when(surveysService.getPotentialSurveyMemberIds(surveyId))
                .thenReturn(List.of(1, 11));
        when(memberRepository.findAllById(List.of(1, 11)))
                .thenReturn(List.of(
                        new MemberEntity(1, "fullName1", "email1", 1, null),
                        new MemberEntity(11, "fullName11", "email11", 0, null)
                ));

        // when
        var result = membersService.getPotentialSurveyMembers(surveyId);

        // then
        var expected = List.of(
                new GetMemberDTO().id(1).fullName("fullName1").emailAddress("email1").isActive(1)
        );
        assertEquals(expected, result);

        verify(memberMapper).toGetMemberDTOs(List.of(
                new MemberEntity(1, "fullName1", "email1", 1, null)
        ));

        verifyNoMoreInteractions(memberRepository);
        verifyNoMoreInteractions(surveysService);

        verifyNoInteractions(participationService);
    }
}