package com.example.demo.mapper;

import com.example.demo.model.MemberEntity;
import generated.openapi.model.GetMemberDTO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MemberMapper {

    GetMemberDTO toGetMemberDTO(MemberEntity source);

    List<GetMemberDTO> toGetMemberDTOs(List<MemberEntity> source);
}
