package com.example.demo.mapper;

import com.example.demo.model.SurveyEntity;
import generated.openapi.model.GetSurveyDTO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface SurveyMapper {

    GetSurveyDTO toGetSurveyDTO(SurveyEntity source);

    List<GetSurveyDTO> toGetSurveyDTOs(List<SurveyEntity> source);
}
