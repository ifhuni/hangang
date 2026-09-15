package com.hangang.web.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import com.hangang.web.admin.AdminUserDetailsService;
import com.hangang.web.vendor.VendorUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain vendorSecurityFilterChain(HttpSecurity http,
                                                           VendorUserDetailsService vendorUserDetailsService,
                                                           PasswordEncoder passwordEncoder) throws Exception {
        http
            .securityMatcher("/vendor/**")
            .authenticationProvider(daoAuthenticationProvider(vendorUserDetailsService, passwordEncoder))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/vendor/signup", "/vendor/login").permitAll()
                .anyRequest().hasRole("VENDOR")
            )
            .formLogin(form -> form
                .loginPage("/vendor/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/vendor/dashboard", true)
                .failureHandler(loginFailureHandler("/vendor/login"))
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/vendor/logout")
                .logoutSuccessUrl("/vendor/login?logout")
                .permitAll()
            );

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http,
                                                          AdminUserDetailsService adminUserDetailsService,
                                                          PasswordEncoder passwordEncoder) throws Exception {
        http
            .securityMatcher("/admin/**")
            .authenticationProvider(daoAuthenticationProvider(adminUserDetailsService, passwordEncoder))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/login").permitAll()
                .anyRequest().hasRole("ADMIN")
            )
            .formLogin(form -> form
                .loginPage("/admin/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/admin/vendors", true)
                .failureHandler(loginFailureHandler("/admin/login"))
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/admin/logout")
                .logoutSuccessUrl("/admin/login?logout")
                .permitAll()
            );

        return http.build();
    }

    @Bean
    @Order(3)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }

    private DaoAuthenticationProvider daoAuthenticationProvider(UserDetailsService userDetailsService,
                                                                  PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    private SimpleUrlAuthenticationFailureHandler loginFailureHandler(String loginPath) {
        return new SimpleUrlAuthenticationFailureHandler(loginPath + "?error") {
            @Override
            public void onAuthenticationFailure(jakarta.servlet.http.HttpServletRequest request,
                                                  jakarta.servlet.http.HttpServletResponse response,
                                                  org.springframework.security.core.AuthenticationException exception)
                    throws java.io.IOException, jakarta.servlet.ServletException {
                if (exception instanceof DisabledException) {
                    setDefaultFailureUrl(loginPath + "?pending");
                } else {
                    setDefaultFailureUrl(loginPath + "?error");
                }
                super.onAuthenticationFailure(request, response, exception);
            }
        };
    }
}
