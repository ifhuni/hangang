package com.hangang.web.application;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ApplicationMapper {

    Application findByToken(String token);

    Application findById(Long applicationId);

    List<Application> findByActivityId(Long activityId);

    List<Application> findByPhoneAndNameAndEmail(@Param("phone") String phone,
                                                  @Param("applicantName") String applicantName,
                                                  @Param("email") String email);

    int insertApplication(Application application);

    int approve(Long applicationId);

    int cancel(String token);
}
