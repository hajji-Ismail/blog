package com.ihajji.backend.profile.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ihajji.backend.profile.dto.FollowerDto;
import com.ihajji.backend.profile.service.ProfileService;
import com.ihajji.backend.user.config.UserPrincipal;

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
    
}
