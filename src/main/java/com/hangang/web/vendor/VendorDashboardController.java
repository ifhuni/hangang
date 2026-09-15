package com.hangang.web.vendor;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VendorDashboardController {

    @GetMapping("/vendor/dashboard")
    public String dashboard(@AuthenticationPrincipal VendorPrincipal principal, Model model) {
        model.addAttribute("vendor", principal.getVendor());
        return "vendor/dashboard";
    }
}
