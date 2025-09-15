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
public class Member {

    @JsonProperty("Member Id")
    private Integer memberId;
    @JsonProperty("Full name")
    private String fullName;
    @JsonProperty("E-mail address")
    private String emailAddress;
    @JsonProperty("Is Active")
    private Integer isActive;
}
