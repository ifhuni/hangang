package com.hangang.web.application;

import java.time.LocalDate;

import jakarta.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hangang.web.activity.ActivityService;

@Controller
public class ApplicationController {

    private final ApplicationService applicationService;
    private final ActivityService activityService;

    public ApplicationController(ApplicationService applicationService, ActivityService activityService) {
        this.applicationService = applicationService;
        this.activityService = activityService;
    }

    @GetMapping("/activities/{activityId}/apply")
    public String applyForm(@PathVariable Long activityId,
                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                             Model model) {
        model.addAttribute("activity", activityService.getActivity(activityId));

        ApplicationForm form = new ApplicationForm();
        form.setParticipationDate(date);
        model.addAttribute("applicationForm", form);
        return "application/apply";
    }

    @PostMapping("/activities/{activityId}/apply")
    public String apply(@PathVariable Long activityId,
                         @Valid @ModelAttribute ApplicationForm applicationForm,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("activity", activityService.getActivity(activityId));
            return "application/apply";
        }

        try {
            String token = applicationService.submitApplication(activityId, applicationForm);
            return "redirect:/apply/" + token + "/complete";
        } catch (IllegalArgumentException | IllegalStateException e) {
            model.addAttribute("activity", activityService.getActivity(activityId));
            model.addAttribute("errorMessage", e.getMessage());
            return "application/apply";
        }
    }

    @GetMapping("/apply/{token}/complete")
    public String complete(@PathVariable String token, Model model) {
        model.addAttribute("myApplication", applicationService.getByToken(token));
        return "application/complete";
    }

    @GetMapping("/apply/{token}")
    public String lookup(@PathVariable String token, Model model) {
        model.addAttribute("myApplication", applicationService.getByToken(token));
        return "application/lookup";
    }

    @PostMapping("/apply/{token}/cancel")
    public String cancel(@PathVariable String token, Model model) {
        try {
            applicationService.cancel(token);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/apply/" + token;
    }
}
