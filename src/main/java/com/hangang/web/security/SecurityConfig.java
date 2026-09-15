package com.hangang.web.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/vendor/signup", "/vendor/login").permitAll()
                .requestMatchers("/vendor/**").hasRole("VENDOR")
                .anyRequest().permitAll()
            )
            .formLogin(form -> form
                .loginPage("/vendor/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/vendor/dashboard", true)
                .failureHandler(loginFailureHandler())
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/vendor/logout")
                .logoutSuccessUrl("/vendor/login?logout")
                .permitAll()
            );

        return http.build();
    }

    private SimpleUrlAuthenticationFailureHandler loginFailureHandler() {
        return new SimpleUrlAuthenticationFailureHandler("/vendor/login?error") {
            @Override
            public void onAuthenticationFailure(jakarta.servlet.http.HttpServletRequest request,
                                                  jakarta.servlet.http.HttpServletResponse response,
                                                  org.springframework.security.core.AuthenticationException exception)
                    throws java.io.IOException, jakarta.servlet.ServletException {
                if (exception instanceof DisabledException) {
                    setDefaultFailureUrl("/vendor/login?pending");
                } else {
                    setDefaultFailureUrl("/vendor/login?error");
                }
                super.onAuthenticationFailure(request, response, exception);
            }
        };
    }
}
