package com.hangang.web.vendor;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class VendorPrincipal implements UserDetails {

    private final Vendor vendor;

    public VendorPrincipal(Vendor vendor) {
        this.vendor = vendor;
    }

    public Vendor getVendor() {
        return vendor;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_VENDOR"));
    }

    @Override
    public String getPassword() {
        return vendor.getPassword();
    }

    @Override
    public String getUsername() {
        return vendor.getEmail();
    }

    @Override
    public boolean isEnabled() {
        return "APPROVED".equals(vendor.getStatus());
    }
}
