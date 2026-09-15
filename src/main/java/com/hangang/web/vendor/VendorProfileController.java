package com.hangang.web.vendor;

import jakarta.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class VendorProfileController {

    private final VendorService vendorService;

    public VendorProfileController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @GetMapping("/vendor/profile")
    public String profileForm(@AuthenticationPrincipal VendorPrincipal principal, Model model) {
        Vendor vendor = vendorService.getProfile(principal.getVendor().getVendorId());

        VendorProfileForm form = new VendorProfileForm();
        form.setBusinessName(vendor.getBusinessName());
        form.setBusinessRegNo(vendor.getBusinessRegNo());
        form.setCeoName(vendor.getCeoName());
        form.setPhone(vendor.getPhone());
        form.setEmail(vendor.getEmail());

        model.addAttribute("vendorProfileForm", form);
        return "vendor/profile";
    }

    @PostMapping("/vendor/profile")
    public String updateProfile(@AuthenticationPrincipal VendorPrincipal principal,
                                 @Valid @ModelAttribute VendorProfileForm vendorProfileForm,
                                 BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            return "vendor/profile";
        }

        try {
            vendorService.updateProfile(principal.getVendor().getVendorId(), vendorProfileForm);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "vendor/profile";
        }

        model.addAttribute("successMessage", "업체 정보가 수정되었습니다.");
        return "vendor/profile";
    }
}
