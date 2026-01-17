package com.ihajji.backend.profile.controller;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.data.javapoet.LordOfTheStrings.ReturnBuilderSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ihajji.backend.profile.dto.FollowerDto;
import com.ihajji.backend.profile.dto.ProfileDto;
import com.ihajji.backend.profile.dto.userDto;
import com.ihajji.backend.profile.service.ProfileService;
import com.ihajji.backend.user.config.UserPrincipal;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("api/v1/user/profile")
public class ProfileController {
    final ProfileService service;
    ProfileController(ProfileService service){
        this.service=service;
    }
@PostMapping("/follow")
public ResponseEntity<FollowerDto> follow(@RequestBody FollowerDto dto,@AuthenticationPrincipal UserPrincipal principal){
FollowerDto response = service.Follow(principal.getUsername(), dto) ;
    return ResponseEntity.status(response.getCode()).body(response);

}
@GetMapping("/user")
public ResponseEntity<userDto> getMethodName(@AuthenticationPrincipal UserPrincipal principal) {
    userDto response = this.service.loadUser(principal.getUsername()) ;
    return ResponseEntity.status(response.getCode()).body(response);
}
@GetMapping("/profile")
public ResponseEntity<ProfileDto> getMethodName(@RequestParam String param) {
    ProfileDto data = this.service.loadProfile(param);
    if (data == null){
        return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(null);
    }
    return ResponseEntity.ok(data);

}




    
}
