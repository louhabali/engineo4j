package com.cinema.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            // 1. Disable CSRF (Stateless REST API)
            .csrf(AbstractHttpConfigurer::disable)
            // 2. Disable CORS at service level (Handled by Gateway)
            .cors(AbstractHttpConfigurer::disable)
            // 3. Allow all requests coming through Gateway
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )
            .build();
    }
}