package com.hangang.web.event;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/events")
public class AdminEventController {

    private final EventService eventService;

    public AdminEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("events", eventService.listForAdmin());
        return "admin/event-list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("eventForm", new EventForm());
        return "admin/event-form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute EventForm eventForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/event-form";
        }
        eventService.createEvent(eventForm);
        return "redirect:/admin/events";
    }

    @GetMapping("/{eventId}/edit")
    public String editForm(@PathVariable Long eventId, Model model) {
        Event event = eventService.getEvent(eventId);

        EventForm form = new EventForm();
        form.setTitle(event.getTitle());
        form.setDescription(event.getDescription());
        form.setLocation(event.getLocation());
        form.setEventDate(event.getEventDate());
        form.setPinned(event.isPinned());
        form.setPublishDate(event.getPublishDate());

        model.addAttribute("eventForm", form);
        model.addAttribute("eventId", eventId);
        return "admin/event-form";
    }

    @PostMapping("/{eventId}")
    public String update(@PathVariable Long eventId,
                          @Valid @ModelAttribute EventForm eventForm,
                          BindingResult bindingResult,
                          Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("eventId", eventId);
            return "admin/event-form";
        }
        eventService.updateEvent(eventId, eventForm);
        return "redirect:/admin/events";
    }

    @PostMapping("/{eventId}/hide")
    public String hide(@PathVariable Long eventId) {
        eventService.setHidden(eventId, true);
        return "redirect:/admin/events";
    }

    @PostMapping("/{eventId}/show")
    public String show(@PathVariable Long eventId) {
        eventService.setHidden(eventId, false);
        return "redirect:/admin/events";
    }
}
