package com.hangang.web.application;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ApplicationMapper {

    Application findByToken(String token);

    Application findById(Long applicationId);

    List<Application> findByActivityId(Long activityId);

    List<ParticipationCount> countByActivity(Long activityId);

    int insertApplication(Application application);

    int approve(Long applicationId);

    int cancel(String token);
}
