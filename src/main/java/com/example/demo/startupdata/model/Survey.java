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
public class Survey {

    @JsonProperty("Survey Id")
    private Integer surveyId;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Expected completes")
    private Integer expectedCompletes;
    @JsonProperty("Completion points")
    private Integer expectedPoints;
    @JsonProperty("Filtered points")
    private Integer filteredPoints;
}
