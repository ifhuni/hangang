package com.hangang.web;

import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.hangang.web.event.Event;
import com.hangang.web.event.EventService;
import com.hangang.web.notice.NoticeService;

@Controller
public class HomeController {

    private static final int NOTICE_LIMIT = 5;
    private static final int EVENT_LIMIT = 3;

    private final NoticeService noticeService;
    private final EventService eventService;

    public HomeController(NoticeService noticeService, EventService eventService) {
        this.noticeService = noticeService;
        this.eventService = eventService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("notices", noticeService.listVisible(NOTICE_LIMIT));

        List<Event> events = eventService.listVisible(EVENT_LIMIT);
        model.addAttribute("events", events);
        model.addAttribute("eventMonthAbbr", events.stream()
                .collect(java.util.stream.Collectors.toMap(
                        Event::getEventId,
                        e -> e.getEventDate().getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase(Locale.ENGLISH))));

        return "home";
    }
}
