package com.ihajji.backend.user.config;

import com.ihajji.backend.user.entity.UserEntity;
import com.ihajji.backend.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class BanCheckFilter extends OncePerRequestFilter {

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var auth = SecurityContextHolder.getContext().getAuthentication();

        // Only process if user is authenticated (e.g. by HTTP Basic or JWT)
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            
            String email = auth.getName();
            UserEntity user = userRepository.findByEmail(email).orElse(null);

            if (user != null) {
                // 1. Check Ban Status
                if (Boolean.TRUE.equals(user.getIs_baned())) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"message\": \"This account is banned\"}");
                    return; 
                }

                // 2. Wrap User in UserPrincipal and update Context
                // This ensures @AuthenticationPrincipal in Controller is NOT null
                UserPrincipal principal = new UserPrincipal(
                    user.getId(), 
                    user.getEmail(), 
                    user.getPassword(), 
                    user.getRole()
                );

                UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(
                        principal, 
                        auth.getCredentials(), 
                        principal.getAuthorities()
                );

                SecurityContextHolder.getContext().setAuthentication(newAuth);
            }
        }

        filterChain.doFilter(request, response);
    }
}