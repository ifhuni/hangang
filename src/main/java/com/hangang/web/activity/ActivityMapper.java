package com.hangang.web.activity;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ActivityMapper {

    Activity findById(Long activityId);

    List<Activity> findByVendorId(Long vendorId);

    List<Activity> search(@Param("keyword") String keyword,
                           @Param("date") LocalDate date,
                           @Param("headcount") Integer headcount);

    int insertActivity(Activity activity);

    int updateActivity(Activity activity);
}
