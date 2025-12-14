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
                                .csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/api/v1/Auth/**").permitAll()

                                                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")

                                                .requestMatchers("/api/v1/user/**").hasAnyRole("USER", "ADMIN")

                                                .anyRequest().authenticated())

                                .httpBasic(httpBasic -> {
                                });

                return http.build();
        }

}