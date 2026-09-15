package com.hangang.web.admin;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AdminUserDetailsService implements UserDetailsService {

    private final AdminMapper adminMapper;

    public AdminUserDetailsService(AdminMapper adminMapper) {
        this.adminMapper = adminMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminMapper.findByUsername(username);
        if (admin == null) {
            throw new UsernameNotFoundException("존재하지 않는 관리자입니다.");
        }
        return new AdminPrincipal(admin);
    }
}
