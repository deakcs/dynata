package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "SURVEY")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class SurveyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "survey_seq_generator")
    @SequenceGenerator(name = "survey_seq_generator", sequenceName = "survey_seq", allocationSize = 1)
    private Integer id;

    private String name;
    private Integer expectedCompletes;
    private Integer expectedPoints;
    private Integer filteredPoints;

    @OneToMany
    @JoinColumn(name = "surveyId")
    private List<ParticipationEntity> participationEntities;
}