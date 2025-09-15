package com.example.demo.startupdata;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.example.demo.model.MemberEntity;
import com.example.demo.model.ParticipationEntity;
import com.example.demo.model.StatusEntity;
import com.example.demo.model.SurveyEntity;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.ParticipationRepository;
import com.example.demo.repository.StatusRepository;
import com.example.demo.repository.SurveyRepository;
import com.example.demo.startupdata.mapper.StartupDataLoaderMapperImpl;
import com.example.demo.startupdata.model.Member;
import com.example.demo.startupdata.model.Participation;
import com.example.demo.startupdata.model.Status;
import com.example.demo.startupdata.model.Survey;
import com.example.demo.startupdata.property.DataLoaderProperties;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StartupDataLoaderTest {

    @InjectMocks
    private StartupDataLoader startupDataLoader;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ParticipationRepository participationRepository;

    @Mock
    private StatusRepository statusRepository;

    @Mock
    private SurveyRepository surveyRepository;

    @Spy
    private StartupDataLoaderMapperImpl startupDataLoaderMapper;

    @Mock
    private DataLoaderProperties dataLoaderProperties;

    private List<Status> getStatuses() {
        return List.of(
                new Status(1, "Not asked"),
                new Status(2, "Rejected"),
                new Status(3, "Filtered"),
                new Status(4, "Completed")
        );
    }

    private List<Member> getMembers() {
        return List.of(
                new Member(1, "Malissa Arn", "MalissaArn0202@gmail.com", 1),
                new Member(2, "Teri Villalobos", "TeriVillalobos6990@gmail.com", 1)
        );
    }

    private List<Survey> getSurveys() {
        return List.of(
                new Survey(1, "Survey 01", 30, 5, 2),
                new Survey(2, "Survey 02", 70, 15, 4),
                new Survey(3, "Survey 03", 100, 10, 2)
        );
    }

    private List<Participation> getParticipations() {
        return List.of(
                new Participation(1, 1, 4, 10),
                new Participation(1, 2, 3, null),
                new Participation(2, 3, 4, 20)
        );
    }

    private List<StatusEntity> getStatusEntities() {
        return List.of(
                new StatusEntity(null, "Not asked", null),
                new StatusEntity(null, "Rejected", null),
                new StatusEntity(null, "Filtered", null),
                new StatusEntity(null, "Completed", null)
        );
    }

    private List<MemberEntity> getMemberEntities() {
        return List.of(
                new MemberEntity(null, "Malissa Arn", "MalissaArn0202@gmail.com", 1, null),
                new MemberEntity(null, "Teri Villalobos", "TeriVillalobos6990@gmail.com", 1, null)
        );
    }

    private List<SurveyEntity> getSurveyEntities() {
        return List.of(
                new SurveyEntity(null, "Survey 01", 30, 5, 2, null),
                new SurveyEntity(null, "Survey 02", 70, 15, 4, null),
                new SurveyEntity(null, "Survey 03", 100, 10, 2, null)
        );
    }


    private List<ParticipationEntity> getParticipationEntities() {
        return List.of(
                new ParticipationEntity(null, 1, 1, 4, 10),
                new ParticipationEntity(null, 1, 2, 3, null),
                new ParticipationEntity(null, 2, 3, 4, 20)
        );
    }

    @Test
    void insertInitialData_allLoaded_ok() {
        // given
        when(statusRepository.count())
                .thenReturn(0L);
        when(dataLoaderProperties.getFileLocation("Statuses.csv"))
                .thenReturn("static/initData/OO - 2 - Statuses.csv");

        when(dataLoaderProperties.getFileLocation("Members.csv"))
                .thenReturn("static/initData/OO - 2 - Members.csv");

        when(dataLoaderProperties.getFileLocation("Surveys.csv"))
                .thenReturn("static/initData/OO - 2 - Surveys.csv");

        when(dataLoaderProperties.getFileLocation("Participation.csv"))
                .thenReturn("static/initData/OO - 2 - Participation.csv");

        // when
        assertDoesNotThrow(() -> startupDataLoader.insertInitialData());

        // then
        verify(startupDataLoaderMapper).toStatusEntities(getStatuses());
        verify(statusRepository).saveAll(getStatusEntities());

        verify(startupDataLoaderMapper).toMemberEntities(getMembers());
        verify(memberRepository).saveAll(getMemberEntities());

        verify(startupDataLoaderMapper).toSurveyEntities(getSurveys());
        verify(surveyRepository).saveAll(getSurveyEntities());

        verify(startupDataLoaderMapper).toParticipationEntities(getParticipations());
        verify(participationRepository).saveAll(getParticipationEntities());
    }

    @Test
    void insertInitialData_nothingLoaded_exception() {
        // given
        when(statusRepository.count())
                .thenReturn(0L);
        when(dataLoaderProperties.getFileLocation("Statuses.csv"))
                .thenReturn("nonExistingFilePath");

        // when
        var result = assertThrows(RuntimeException.class, () -> startupDataLoader.insertInitialData());

        // then
        var expected = new RuntimeException("Could not load file: nonExistingFilePath");
        assertEquals(expected.getMessage(), result.getMessage());

        verifyNoMoreInteractions(dataLoaderProperties);
        verifyNoInteractions(startupDataLoaderMapper);

        verifyNoMoreInteractions(statusRepository);
        verifyNoInteractions(memberRepository);
        verifyNoInteractions(surveyRepository);
        verifyNoInteractions(participationRepository);
    }
}