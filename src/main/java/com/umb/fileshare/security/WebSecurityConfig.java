package com.umb.fileshare.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
        private final JwtAuthenticationFilter jetAuthenticationFilter;

        private final CustomUserDetailService customUserDetailService;

        @Bean
        public SecurityFilterChain applicationSecurity(HttpSecurity http) throws Exception {
                http.addFilterBefore(jetAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

                http
                                .cors(cors -> cors.disable())
                                .csrf(csrf -> csrf.disable())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .formLogin(formLogin -> formLogin.disable())
                                .securityMatcher("/**")
                                .authorizeHttpRequests(registry -> registry
                                                .requestMatchers("/api/auth/*").permitAll()
                                                .anyRequest().authenticated());

                return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
                AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
                builder.userDetailsService(customUserDetailService)
                                .passwordEncoder(passwordEncoder());

                return builder.build();
        }
}
