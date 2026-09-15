package com.hangang.web.application;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hangang.web.activity.Activity;
import com.hangang.web.activity.ActivityService;
import com.hangang.web.vendor.VendorPrincipal;

@Controller
@RequestMapping("/vendor/activities/{activityId}/participants")
public class VendorParticipantController {

    private final ApplicationService applicationService;
    private final ActivityService activityService;

    public VendorParticipantController(ApplicationService applicationService, ActivityService activityService) {
        this.applicationService = applicationService;
        this.activityService = activityService;
    }

    @GetMapping
    public String list(@AuthenticationPrincipal VendorPrincipal principal,
                        @PathVariable Long activityId,
                        Model model) {
        Long vendorId = principal.getVendor().getVendorId();
        Activity activity = activityService.getVendorActivity(vendorId, activityId);
        List<Application> applications = applicationService.listByActivityForVendor(vendorId, activityId);

        model.addAttribute("activity", activity);
        model.addAttribute("applications", applications);
        model.addAttribute("summaries", applicationService.summarizeByDate(activity, applications));
        return "vendor/participants";
    }

    @PostMapping("/{applicationId}/approve")
    public String approve(@AuthenticationPrincipal VendorPrincipal principal,
                           @PathVariable Long activityId,
                           @PathVariable Long applicationId,
                           RedirectAttributes redirectAttributes) {
        try {
            applicationService.approveForVendor(principal.getVendor().getVendorId(), activityId, applicationId);
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/vendor/activities/" + activityId + "/participants";
    }
}
