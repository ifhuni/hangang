package com.hangang.web.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/login")
    public String loginForm() {
        return "admin/login";
    }

    @GetMapping("/vendors")
    public String pendingVendors(Model model) {
        model.addAttribute("vendors", adminService.listPendingVendors());
        return "admin/vendors";
    }

    @PostMapping("/vendors/{vendorId}/approve")
    public String approve(@PathVariable Long vendorId) {
        adminService.approveVendor(vendorId);
        return "redirect:/admin/vendors";
    }

    @PostMapping("/vendors/{vendorId}/reject")
    public String reject(@PathVariable Long vendorId) {
        adminService.rejectVendor(vendorId);
        return "redirect:/admin/vendors";
    }
}
