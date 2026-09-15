package com.hangang.web.notice;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class NoticeService {

    private final NoticeMapper noticeMapper;

    public NoticeService(NoticeMapper noticeMapper) {
        this.noticeMapper = noticeMapper;
    }

    public List<Notice> listForAdmin() {
        return noticeMapper.findAllForAdmin();
    }

    public List<Notice> listVisible(int limit) {
        return noticeMapper.findVisible(limit);
    }

    public Notice getNotice(Long noticeId) {
        Notice notice = noticeMapper.findById(noticeId);
        if (notice == null) {
            throw new IllegalArgumentException("존재하지 않는 공지사항입니다.");
        }
        return notice;
    }

    public void createNotice(NoticeForm form) {
        Notice notice = new Notice();
        applyForm(notice, form);
        noticeMapper.insertNotice(notice);
    }

    public void updateNotice(Long noticeId, NoticeForm form) {
        Notice notice = new Notice();
        notice.setNoticeId(noticeId);
        applyForm(notice, form);
        noticeMapper.updateNotice(notice);
    }

    public void setHidden(Long noticeId, boolean hidden) {
        noticeMapper.updateHidden(noticeId, hidden);
    }

    private void applyForm(Notice notice, NoticeForm form) {
        notice.setTitle(form.getTitle());
        notice.setContent(form.getContent());
        notice.setPinned(form.isPinned());
        notice.setPublishDate(form.getPublishDate());
    }
}
