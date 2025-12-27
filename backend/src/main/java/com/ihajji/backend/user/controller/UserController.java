package com.ihajji.backend.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ihajji.backend.user.dto.AuthResponse;
import com.ihajji.backend.user.dto.CreateUserDto;
import com.ihajji.backend.user.dto.ErrorResponse;
import com.ihajji.backend.user.dto.UserCredentiales;
import com.ihajji.backend.user.service.UserService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.Map;
import java.util.HashMap;


import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("api/v1/Auth")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;

    }


@PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<?> register(@Valid @ModelAttribute CreateUserDto dto, BindingResult bindingResult) {
    
    // This will tell you EXACTLY why the data isn't marshalling
    if (bindingResult.hasErrors()) {
        Map<String, String> errors = new HashMap<>();
        bindingResult.getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }

    try {
     
        AuthResponse saved = userService.register(dto);
        // ... rest of your cookie and response logic
        return ResponseEntity.ok(saved);
    } catch (Exception e) {
        return ResponseEntity.internalServerError().body(e.getMessage());
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
                    .status(HttpStatus.OK)
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .body(new ErrorResponse("log in went smoothly"));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e);
        }

    }
   

   
    

}
