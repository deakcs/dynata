package com.example.demo.service;

import static com.example.demo.helper.Constants.COMPLETED;
import static com.example.demo.helper.Constants.FILTERED;

import com.example.demo.exception.NotFoundException;
import com.example.demo.mapper.MemberMapper;
import com.example.demo.model.MemberEntity;
import com.example.demo.model.ParticipationEntity;
import com.example.demo.repository.MemberRepository;
import generated.openapi.model.GetMemberDTO;
import generated.openapi.model.GetMemberPointsDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MembersService {

    private static final Integer INACTIVE = 1;

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    private final ParticipationService participationService;
    private final SurveysService surveysService;

    public List<GetMemberDTO> getMembersBySurveyIdAndStatusId(int surveyId, int statusId) {
        var memberIds = participationService.getMemberIdsBySurveyIdAndStatusId(surveyId, statusId);
        var memberEntities = memberRepository.findAllById(memberIds);

        return memberMapper.toGetMemberDTOs(memberEntities);
    }

    public List<GetMemberPointsDTO> getMemberPoints(int memberId) {
        var memberEntity = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(
                        "Could not find record.",
                        MemberEntity.class.getSimpleName(),
                        "id",
                        String.valueOf(memberId))
                );

        var surveyIds = memberEntity.getParticipationEntities()
                .stream()
                .filter(p -> COMPLETED == p.getStatusId() || FILTERED == p.getStatusId())
                .map(ParticipationEntity::getSurveyId)
                .toList();

        /*
            TODO: what should be put into collectedPoints?
                expectedCompletes OR expectedPoints OR filteredPoints OR some calculated value?
         */
        return surveysService.getSurveys(surveyIds)
                .stream()
                .map(s -> new GetMemberPointsDTO()
                        .surveyId(s.getId())
                        .collectedPoints(s.getExpectedCompletes()))
                .toList();
    }

    public List<GetMemberDTO> getPotentialSurveyMembers(int surveyId) {
        var memberIds = surveysService.getPotentialSurveyMemberIds(surveyId);

        var memberEntities = memberRepository.findAllById(memberIds)
                .stream()
                .filter(m -> INACTIVE.equals(m.getIsActive()))
                .toList();

        return memberMapper.toGetMemberDTOs(memberEntities);
    }
}
