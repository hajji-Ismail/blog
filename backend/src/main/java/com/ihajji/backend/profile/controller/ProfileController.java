package com.ihajji.backend.profile.controller;

import java.util.List;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ihajji.backend.profile.dto.FollowerDto;
import com.ihajji.backend.profile.dto.FollowerErrorDto;
import com.ihajji.backend.profile.dto.ProfileDto;
import com.ihajji.backend.profile.dto.userDto;
import com.ihajji.backend.profile.service.ProfileService;
import com.ihajji.backend.user.config.UserPrincipal;
import com.ihajji.backend.user.dto.SearchDto;
import com.ihajji.backend.user.service.UserService;



@RestController
@RequestMapping("api/v1/user/profile")
public class ProfileController {
    final ProfileService service;
    final UserService userservice;
    ProfileController(ProfileService service, UserService userservice){
        this.service=service;
        this.userservice = userservice;
    }
@PostMapping("/follow")
public ResponseEntity<FollowerErrorDto> follow(@RequestBody FollowerDto dto,@AuthenticationPrincipal UserPrincipal principal){
FollowerErrorDto response = service.Follow(principal.getUsername(), dto) ;
    return ResponseEntity.status(response.getCode()).body(response);

}
@GetMapping("/user")
public ResponseEntity<userDto> getMethodName(@AuthenticationPrincipal UserPrincipal principal) {
    userDto response = this.service.loadUser(principal.getUsername()) ;
    return ResponseEntity.status(response.getCode()).body(response);
}
@GetMapping("/profile")
public ResponseEntity<ProfileDto> getMethodName(@RequestParam String param , @AuthenticationPrincipal UserPrincipal principal) {
    ProfileDto data = this.service.loadProfile(param, principal.getUsername());
    if (data == null){
        return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(null);
    }
    return ResponseEntity.ok(data);

}

@GetMapping("/search")
public ResponseEntity<List<SearchDto>> search(@RequestParam String param) {
    return ResponseEntity.ok().body(this.userservice.Search(param)) ;
}



    
}
