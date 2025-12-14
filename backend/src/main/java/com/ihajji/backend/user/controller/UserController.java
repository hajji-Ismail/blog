package com.ihajji.backend.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ihajji.backend.user.dto.AuthResponse;
import com.ihajji.backend.user.dto.CreateUserDto;
import com.ihajji.backend.user.dto.ErrorResponse;
import com.ihajji.backend.user.dto.UserCredentiales;
import com.ihajji.backend.user.entity.UserEntity;
import com.ihajji.backend.user.service.UserService;
import com.ihajji.backend.user.utils.JwtUtil;

import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.HttpHeaders;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("api/v1/Auth")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwt ;

    public UserController(UserService userService, JwtUtil jwt ) {
        this.userService = userService;
        this.jwt = jwt;

    }

 
@PostMapping("/register")
public ResponseEntity<?> register(@Valid @RequestBody CreateUserDto dto) {
    try {
        UserEntity saved = userService.register(dto);

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "user");

        String token = jwt.generateToken(claims, dto.getUsername());

        ResponseCookie jwtCookie = ResponseCookie.from("JWT", token)
                .httpOnly(true)        // NOT accessible from JS
                .secure(false)         // true in production (HTTPS)
                .path("/")             // available for whole app
                .maxAge(24 * 60 * 60)  // 1 day
                .sameSite("Lax")       // recommended for auth cookies
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(saved);

    } catch (ErrorResponse e) {
        return ResponseEntity.badRequest().body(e);
    }
}
@PostMapping("/login")
public ResponseEntity<?>login(@RequestBody UserCredentiales dto) {

    try {
        AuthResponse AuthResponse = userService.login(dto);
        ResponseCookie jwtCookie = ResponseCookie.from("JWT",AuthResponse.getAccessToken())
            .httpOnly(true)
            .secure(true) 
            .path("/")
            .maxAge(24 * 60 * 60)
            .sameSite("Lax")
            .build();
        
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(AuthResponse);

      
        
    } catch (Exception e) {
         return ResponseEntity.badRequest().body(e);
    }
  
    
    
}



}
