package com.ihajji.backend.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class UserConfig {
      @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Lambda syntax
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/Auth/**").permitAll()
                        .anyRequest().authenticated())
                .httpBasic(httpBasic -> {}); // optional for testing

        return http.build();
    }
    
}



