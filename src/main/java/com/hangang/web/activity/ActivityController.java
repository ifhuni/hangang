package com.hangang.web.activity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.hangang.web.application.ApplicationService;

@Controller
public class ActivityController {

    private final ActivityService activityService;
    private final ApplicationService applicationService;

    public ActivityController(ActivityService activityService, ApplicationService applicationService) {
        this.activityService = activityService;
        this.applicationService = applicationService;
    }

    @GetMapping({"/", "/activities"})
    public String list(@RequestParam(required = false) String keyword, Model model) {
        model.addAttribute("activities", activityService.listPublicActivities(keyword));
        model.addAttribute("keyword", keyword);
        return "activity/list";
    }

    @GetMapping("/activities/{activityId}")
    public String detail(@PathVariable Long activityId, Model model) {
        Activity activity = activityService.getActivity(activityId);
        Map<LocalDate, Integer> appliedCounts = applicationService.getAppliedCounts(activityId);

        List<ActivityDateRow> dateRows = new ArrayList<>();
        for (LocalDate date : datesBetween(activity.getActivityStartDate(), activity.getActivityEndDate())) {
            dateRows.add(new ActivityDateRow(date, activity.getDailyCapacity(), appliedCounts.getOrDefault(date, 0)));
        }

        model.addAttribute("activity", activity);
        model.addAttribute("dateRows", dateRows);
        return "activity/detail";
    }

    private List<LocalDate> datesBetween(LocalDate start, LocalDate end) {
        List<LocalDate> dates = new ArrayList<>();
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            dates.add(date);
        }
        return dates;
    }
}
