package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "PARTICIPATION")
@DynamicUpdate
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ParticipationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "participation_seq_generator")
    @SequenceGenerator(name = "participation_seq_generator", sequenceName = "participation_seq", allocationSize = 1)
    private Integer id;

    private Integer memberId;
    private Integer surveyId;
    private Integer statusId;
    private Integer length;
}
