package com.ihajji.backend.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

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
                                }).logout(logout -> logout
                                                .logoutUrl("/api/v1/Auth/logout")
                                                .invalidateHttpSession(true)
                                                .deleteCookies("JWT")

                                                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(
                                                                HttpStatus.OK)));

                return http.build();
        }

}