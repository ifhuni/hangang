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

    public Vendor getProfile(Long vendorId) {
        Vendor vendor = vendorMapper.findById(vendorId);
        if (vendor == null) {
            throw new IllegalArgumentException("존재하지 않는 업체입니다.");
        }
        return vendor;
    }

    public void updateProfile(Long vendorId, VendorProfileForm form) {
        if (vendorMapper.existsByEmailExcludingVendor(form.getEmail(), vendorId) > 0) {
            throw new IllegalStateException("이미 사용 중인 이메일입니다.");
        }

        Vendor vendor = new Vendor();
        vendor.setVendorId(vendorId);
        vendor.setBusinessName(form.getBusinessName());
        vendor.setBusinessRegNo(form.getBusinessRegNo());
        vendor.setCeoName(form.getCeoName());
        vendor.setPhone(form.getPhone());
        vendor.setEmail(form.getEmail());
        vendorMapper.updateProfile(vendor);

        if (form.getNewPassword() != null && !form.getNewPassword().isBlank()) {
            vendorMapper.updatePassword(vendorId, passwordEncoder.encode(form.getNewPassword()));
        }
    }
}
