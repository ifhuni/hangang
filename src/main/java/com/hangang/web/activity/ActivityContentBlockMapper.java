package com.hangang.web.activity;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ActivityContentBlockMapper {

    List<ActivityContentBlock> findByActivityId(Long activityId);

    void insertBlock(ActivityContentBlock block);

    void deleteByActivityId(Long activityId);
}
