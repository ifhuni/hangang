package com.hangang.web.application;

import java.time.LocalDate;
import java.util.List;

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

    @GetMapping("/apply/lookup")
    public String searchForm() {
        return "application/lookup-search";
    }

    @PostMapping("/apply/lookup")
    public String search(@RequestParam String phone, @RequestParam String applicantName,
                          @RequestParam String email, Model model) {
        List<Application> matches = applicationService.searchApplications(phone, applicantName, email);

        if (matches.isEmpty()) {
            model.addAttribute("errorMessage", "일치하는 신청 내역이 없습니다. 입력한 정보를 다시 확인해주세요.");
            return "application/lookup-search";
        }
        if (matches.size() == 1) {
            return "redirect:/apply/" + matches.get(0).getToken();
        }

        model.addAttribute("matches", matches);
        return "application/lookup-results";
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
