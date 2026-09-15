package com.hangang.web.vendor;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/vendor")
public class VendorController {

    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("vendorSignupForm", new VendorSignupForm());
        return "vendor/signup";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "vendor/login";
    }

    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute VendorSignupForm vendorSignupForm,
                          BindingResult bindingResult,
                          Model model) {
        if (bindingResult.hasErrors()) {
            return "vendor/signup";
        }

        try {
            vendorService.signup(vendorSignupForm);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "vendor/signup";
        }

        return "vendor/signup-complete";
    }
}
