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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("api/v1/Auth")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwt;

    public UserController(UserService userService, JwtUtil jwt) {
        this.userService = userService;
        this.jwt = jwt;

    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody CreateUserDto dto) {
        try {
            AuthResponse saved = userService.register(dto);
            ResponseCookie jwtCookie = ResponseCookie.from("JWT", saved.getAccessToken())
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(24 * 60 * 60)
                    .sameSite("Lax")
                    .build();

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .body(new ErrorResponse("regester went smoothly"));

        } catch (ErrorResponse e) {
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserCredentiales dto) {

        try {
            AuthResponse AuthResponse = userService.login(dto);
            ResponseCookie jwtCookie = ResponseCookie.from("JWT", AuthResponse.getAccessToken())
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(24 * 60 * 60)
                    .sameSite("Lax")
                    .build();

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .body(new ErrorResponse("log in went smoothly"));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e);
        }

    }
   

   
    

}
