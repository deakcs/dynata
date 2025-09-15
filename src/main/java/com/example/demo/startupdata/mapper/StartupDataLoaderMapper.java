package com.example.demo.startupdata.mapper;

import com.example.demo.model.MemberEntity;
import com.example.demo.model.ParticipationEntity;
import com.example.demo.model.StatusEntity;
import com.example.demo.model.SurveyEntity;
import com.example.demo.startupdata.model.Member;
import com.example.demo.startupdata.model.Participation;
import com.example.demo.startupdata.model.Status;
import com.example.demo.startupdata.model.Survey;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface StartupDataLoaderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "participationEntities", ignore = true)
    MemberEntity toMemberEntity(Member source);

    List<MemberEntity> toMemberEntities(List<Member> source);

    @Mapping(target = "id", ignore = true)
    ParticipationEntity toParticipationEntity(Participation source);

    List<ParticipationEntity> toParticipationEntities(List<Participation> source);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "participationEntities", ignore = true)
    StatusEntity toStatusEntity(Status source);

    List<StatusEntity> toStatusEntities(List<Status> source);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "participationEntities", ignore = true)
    SurveyEntity toSurveyEntity(Survey source);

    List<SurveyEntity> toSurveyEntities(List<Survey> source);
}
