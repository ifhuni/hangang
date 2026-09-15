package com.hangang.web.event;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface EventMapper {

    List<Event> findAllForAdmin();

    List<Event> findVisible(@Param("limit") int limit);

    Event findById(Long eventId);

    int insertEvent(Event event);

    int updateEvent(Event event);

    int updateHidden(@Param("eventId") Long eventId, @Param("hidden") boolean hidden);
}
