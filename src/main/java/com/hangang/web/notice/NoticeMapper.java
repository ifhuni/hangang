package com.hangang.web.notice;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface NoticeMapper {

    List<Notice> findAllForAdmin();

    List<Notice> findVisible(@Param("limit") int limit);

    Notice findById(Long noticeId);

    int insertNotice(Notice notice);

    int updateNotice(Notice notice);

    int updateHidden(@Param("noticeId") Long noticeId, @Param("hidden") boolean hidden);
}
