package com.example.demo.startupdata.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Participation {

    @JsonProperty("Member Id")
    private Integer memberId;
    @JsonProperty("Survey Id")
    private Integer surveyId;
    @JsonProperty("Status")
    private Integer statusId;
    @JsonProperty("Length")
    private Integer length;
}
