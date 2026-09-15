package com.hangang.web.application;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ApplicationMapper {

    Application findByToken(String token);

    List<ParticipationCount> countByActivity(Long activityId);

    int insertApplication(Application application);

    int cancel(String token);
}
