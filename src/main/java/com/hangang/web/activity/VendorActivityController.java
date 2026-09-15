package com.hangang.web.activity;

import jakarta.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hangang.web.vendor.VendorPrincipal;

@Controller
@RequestMapping("/vendor/activities")
public class VendorActivityController {

    private final ActivityService activityService;

    public VendorActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping
    public String list(@AuthenticationPrincipal VendorPrincipal principal, Model model) {
        model.addAttribute("activities", activityService.listVendorActivities(principal.getVendor().getVendorId()));
        return "vendor/activity-list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("activityForm", new ActivityForm());
        return "vendor/activity-form";
    }

    @PostMapping
    public String create(@AuthenticationPrincipal VendorPrincipal principal,
                          @Valid @ModelAttribute ActivityForm activityForm,
                          BindingResult bindingResult,
                          Model model) {
        if (bindingResult.hasErrors()) {
            return "vendor/activity-form";
        }

        try {
            activityService.registerActivity(principal.getVendor().getVendorId(), activityForm);
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "vendor/activity-form";
        }

        return "redirect:/vendor/activities";
    }

    @GetMapping("/{activityId}/edit")
    public String editForm(@AuthenticationPrincipal VendorPrincipal principal,
                            @PathVariable Long activityId,
                            Model model) {
        Activity activity = activityService.getVendorActivity(principal.getVendor().getVendorId(), activityId);

        ActivityForm form = new ActivityForm();
        form.setTitle(activity.getTitle());
        form.setDescription(activity.getDescription());
        form.setPrice(activity.getPrice());
        form.setRegistrationStartDate(activity.getRegistrationStartDate());
        form.setRegistrationEndDate(activity.getRegistrationEndDate());
        form.setActivityStartDate(activity.getActivityStartDate());
        form.setActivityEndDate(activity.getActivityEndDate());
        form.setDailyCapacity(activity.getDailyCapacity());

        model.addAttribute("activityForm", form);
        model.addAttribute("activityId", activityId);
        return "vendor/activity-form";
    }

    @PostMapping("/{activityId}")
    public String update(@AuthenticationPrincipal VendorPrincipal principal,
                          @PathVariable Long activityId,
                          @Valid @ModelAttribute ActivityForm activityForm,
                          BindingResult bindingResult,
                          Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("activityId", activityId);
            return "vendor/activity-form";
        }

        try {
            activityService.updateActivity(principal.getVendor().getVendorId(), activityId, activityForm);
        } catch (IllegalArgumentException | IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("activityId", activityId);
            return "vendor/activity-form";
        }

        return "redirect:/vendor/activities";
    }
}
