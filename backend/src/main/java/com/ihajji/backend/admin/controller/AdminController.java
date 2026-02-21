package com.ihajji.backend.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ihajji.backend.admin.dto.AdminDataDto;
import com.ihajji.backend.admin.dto.AdminErrorDto;
import com.ihajji.backend.admin.dto.AdminUserDto;
import com.ihajji.backend.admin.service.AdminService;
import com.ihajji.backend.user.config.UserPrincipal;



@RestController
@RequestMapping("api/v1/admin")
public class AdminController {
    final AdminService service ;
    AdminController( AdminService service){
        this.service = service;
    }
    @PostMapping("/baneUser")
    public ResponseEntity<AdminErrorDto> BaneUser(@RequestBody AdminUserDto entity) {
        AdminErrorDto response = service.BannedUser(entity);

        
        return ResponseEntity.status(response.getCode()).body(response);
    }
    @PostMapping("/unbaneUser")
    public ResponseEntity<AdminErrorDto> UnBaneUser(@RequestBody AdminUserDto entity) {
        AdminErrorDto response = service.UnBannedUser(entity);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/deletPost")
    public ResponseEntity<AdminErrorDto> DeletePost(@RequestBody Long entity) {
        AdminErrorDto response = service.DeletePost(entity);
        return ResponseEntity.status(response.getCode()).body(response);
    }
    @GetMapping("load")
    public ResponseEntity<AdminDataDto> load( ) {
        return ResponseEntity.ok().body(this.service.Load());
    }
    @PostMapping("deletUser")
    public ResponseEntity<AdminErrorDto> DeletUser(String username, @AuthenticationPrincipal UserPrincipal principal){
        AdminErrorDto data = this.service.delet(username, principal.getUsername());
        return ResponseEntity.status(data.getCode()).body(data);

    }
    
    
    
    
}
