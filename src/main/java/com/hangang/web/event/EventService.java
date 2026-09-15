package com.hangang.web.event;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class EventService {

    private final EventMapper eventMapper;

    public EventService(EventMapper eventMapper) {
        this.eventMapper = eventMapper;
    }

    public List<Event> listForAdmin() {
        return eventMapper.findAllForAdmin();
    }

    public List<Event> listVisible(int limit) {
        return eventMapper.findVisible(limit);
    }

    public Event getEvent(Long eventId) {
        Event event = eventMapper.findById(eventId);
        if (event == null) {
            throw new IllegalArgumentException("존재하지 않는 이벤트입니다.");
        }
        return event;
    }

    public void createEvent(EventForm form) {
        Event event = new Event();
        applyForm(event, form);
        eventMapper.insertEvent(event);
    }

    public void updateEvent(Long eventId, EventForm form) {
        Event event = new Event();
        event.setEventId(eventId);
        applyForm(event, form);
        eventMapper.updateEvent(event);
    }

    public void setHidden(Long eventId, boolean hidden) {
        eventMapper.updateHidden(eventId, hidden);
    }

    private void applyForm(Event event, EventForm form) {
        event.setTitle(form.getTitle());
        event.setDescription(form.getDescription());
        event.setLocation(form.getLocation());
        event.setEventDate(form.getEventDate());
        event.setPinned(form.isPinned());
        event.setPublishDate(form.getPublishDate());
    }
}
