package com.hangang.web.vendor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.hangang.web.activity.Activity;
import com.hangang.web.activity.ActivityService;
import com.hangang.web.application.Application;
import com.hangang.web.application.ApplicationService;

@Controller
public class VendorDashboardController {

    private final ActivityService activityService;
    private final ApplicationService applicationService;

    public VendorDashboardController(ActivityService activityService, ApplicationService applicationService) {
        this.activityService = activityService;
        this.applicationService = applicationService;
    }

    @GetMapping("/vendor/dashboard")
    public String dashboard(@AuthenticationPrincipal VendorPrincipal principal, Model model) {
        Long vendorId = principal.getVendor().getVendorId();
        List<Activity> activities = activityService.listVendorActivities(vendorId);

        List<VendorActivityCard> cards = new ArrayList<>();
        int totalPending = 0;
        int totalApproved = 0;

        for (Activity activity : activities) {
            List<Application> applications = applicationService.listByActivityForVendor(vendorId, activity.getActivityId());
            int pending = (int) applications.stream().filter(a -> "PENDING".equals(a.getStatus())).count();
            int approved = (int) applications.stream().filter(a -> "APPROVED".equals(a.getStatus())).count();
            cards.add(new VendorActivityCard(activity, pending, approved));
            totalPending += pending;
            totalApproved += approved;
        }

        model.addAttribute("vendor", principal.getVendor());
        model.addAttribute("activityCards", cards);
        model.addAttribute("totalActivities", activities.size());
        model.addAttribute("totalApplicants", totalPending + totalApproved);
        model.addAttribute("totalPending", totalPending);
        model.addAttribute("totalApproved", totalApproved);
        return "vendor/dashboard";
    }
}
