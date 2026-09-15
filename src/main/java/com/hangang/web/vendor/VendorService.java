package com.hangang.web.vendor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class VendorService {

    private final VendorMapper vendorMapper;
    private final PasswordEncoder passwordEncoder;

    public VendorService(VendorMapper vendorMapper, PasswordEncoder passwordEncoder) {
        this.vendorMapper = vendorMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public void signup(VendorSignupForm form) {
        if (vendorMapper.existsByEmail(form.getEmail()) > 0) {
            throw new IllegalStateException("이미 가입된 이메일입니다.");
        }

        Vendor vendor = new Vendor();
        vendor.setBusinessName(form.getBusinessName());
        vendor.setBusinessRegNo(form.getBusinessRegNo());
        vendor.setCeoName(form.getCeoName());
        vendor.setPhone(form.getPhone());
        vendor.setEmail(form.getEmail());
        vendor.setPassword(passwordEncoder.encode(form.getPassword()));

        vendorMapper.insertVendor(vendor);
    }
}
