package com.hangang.web.vendor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class VendorUserDetailsService implements UserDetailsService {

    private final VendorMapper vendorMapper;

    public VendorUserDetailsService(VendorMapper vendorMapper) {
        this.vendorMapper = vendorMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Vendor vendor = vendorMapper.findByEmail(email);
        if (vendor == null) {
            throw new UsernameNotFoundException("가입되지 않은 이메일입니다.");
        }
        return new VendorPrincipal(vendor);
    }
}
