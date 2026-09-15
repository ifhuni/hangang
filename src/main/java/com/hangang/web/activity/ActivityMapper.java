package com.hangang.web.activity;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ActivityMapper {

    Activity findById(Long activityId);

    List<Activity> findByVendorId(Long vendorId);

    List<Activity> search(@Param("keyword") String keyword);

    int insertActivity(Activity activity);

    int updateActivity(Activity activity);
}
