package com.ihajji.backend.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ihajji.backend.user.dto.AuthResponse;
import com.ihajji.backend.user.dto.CreateUserDto;
import com.ihajji.backend.user.dto.UserCredentiales;
import com.ihajji.backend.user.dto.UserErrorsDto;
import com.ihajji.backend.user.service.UserService;

import jakarta.validation.Valid;


import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpHeaders;

import org.springframework.http.MediaType;




@RestController
@RequestMapping("api/v1/Auth")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;

    }


@PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<AuthResponse> register(@Valid @ModelAttribute CreateUserDto dto, BindingResult bindingResult) {
    
    // This will tell you EXACTLY why the data isn't marshalling
    if (bindingResult.hasErrors()) {
        AuthResponse err= new AuthResponse(new UserErrorsDto(500, "Marshling err", null, null, null, null));
      return ResponseEntity.internalServerError().body(err);

    }

 
     
        AuthResponse saved = userService.register(dto);
        if (saved.getUser().getCode() != 200){
            return ResponseEntity.status(saved.getUser().getCode()).body(saved);
        }
       ResponseCookie jwtCookie = ResponseCookie.from("JWT", saved.getAccessToken())
                    .httpOnly(false)
                    .secure(false)
                    .path("/")
                    .maxAge(24 * 60 * 60)
                    .sameSite("Lax")
                    .build();
                    System.out.println("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
    
   return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, jwtCookie.toString()) // Sends cookie to browser
            .body(saved);
       
   
}

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody UserCredentiales dto) {

 
            AuthResponse AuthResponse = userService.login(dto);
            if (AuthResponse.getUser().getCode() != 200){
                return ResponseEntity.status(AuthResponse.getUser().getCode()).body(AuthResponse);
            }
            ResponseCookie jwtCookie = ResponseCookie.from("JWT", AuthResponse.getAccessToken())
                    .httpOnly(false)
                    .secure(false)
                    .path("/")
                    .maxAge(24 * 60 * 60)
                    .sameSite("Lax")
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .body(AuthResponse);

        
    }

   
    

}
