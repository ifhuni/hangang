package com.hangang.web.activity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
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

    @GetMapping("/activities")
    public String list(@RequestParam(required = false) String keyword,
                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                        @RequestParam(required = false) Integer headcount,
                        @RequestParam(required = false) String sort,
                        Model model) {
        List<Activity> activities = activityService.listPublicActivities(keyword, date, headcount, sort);

        Map<Long, Integer> remainingByActivityId = null;
        if (date != null) {
            remainingByActivityId = new HashMap<>();
            for (Activity activity : activities) {
                int applied = applicationService.getAppliedCounts(activity.getActivityId()).getOrDefault(date, 0);
                remainingByActivityId.put(activity.getActivityId(), activity.getDailyCapacity() - applied);
            }
        }

        model.addAttribute("activities", activities);
        model.addAttribute("keyword", keyword);
        model.addAttribute("date", date);
        model.addAttribute("headcount", headcount);
        model.addAttribute("sort", sort);
        model.addAttribute("remainingByActivityId", remainingByActivityId);
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
