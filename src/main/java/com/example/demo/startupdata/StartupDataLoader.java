package com.example.demo.startupdata;

import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.ParticipationRepository;
import com.example.demo.repository.StatusRepository;
import com.example.demo.repository.SurveyRepository;
import com.example.demo.startupdata.mapper.StartupDataLoaderMapper;
import com.example.demo.startupdata.model.Member;
import com.example.demo.startupdata.model.Participation;
import com.example.demo.startupdata.model.Status;
import com.example.demo.startupdata.model.Survey;
import com.example.demo.startupdata.property.DataLoaderProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class StartupDataLoader {

    private final MemberRepository memberRepository;
    private final ParticipationRepository participationRepository;
    private final StatusRepository statusRepository;
    private final SurveyRepository surveyRepository;

    private final StartupDataLoaderMapper startupDataLoaderMapper;

    private final DataLoaderProperties dataLoaderProperties;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void insertInitialData() {
        if (0 != statusRepository.count()) {
            log.info("Skip loading Initial Data: Already loaded.");
            return;
        }

        log.info("Start inserting initial data.");

        var fileName = dataLoaderProperties.getFileLocation("Statuses.csv");
        var statuses = ObjectLoader.loadObjectList(Status.class, fileName);
        var statusEntities = startupDataLoaderMapper.toStatusEntities(statuses);

        fileName = dataLoaderProperties.getFileLocation("Members.csv");
        var members = ObjectLoader.loadObjectList(Member.class, fileName);
        var memberEntities = startupDataLoaderMapper.toMemberEntities(members);

        fileName = dataLoaderProperties.getFileLocation("Surveys.csv");
        var surveys = ObjectLoader.loadObjectList(Survey.class, fileName);
        var surveyEntities = startupDataLoaderMapper.toSurveyEntities(surveys);

        fileName = dataLoaderProperties.getFileLocation("Participation.csv");
        var participations = ObjectLoader.loadObjectList(Participation.class, fileName);
        var participationEntities = startupDataLoaderMapper.toParticipationEntities(participations);

        statusRepository.saveAll(statusEntities);
        log.info("Successfully processed file: [{}] and created [{}] StatusEntity records from file",
                fileName, statusEntities.size());

        memberRepository.saveAll(memberEntities);
        log.info("Successfully processed file: [{}] and created [{}] MemberEntity records from file",
                fileName, memberEntities.size());

        surveyRepository.saveAll(surveyEntities);
        log.info("Successfully processed file: [{}] and created [{}] SurveyEntity records from file",
                fileName, surveyEntities.size());

        participationRepository.saveAll(participationEntities);
        log.info("Successfully processed file: [{}] and created [{}] ParticipationEntity records from file",
                fileName, participationEntities.size());

        log.info("Done inserting initial data.");
    }
}