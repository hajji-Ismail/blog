package com.ihajji.backend.admin.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ihajji.backend.admin.dto.AdminErrorDto;
import com.ihajji.backend.admin.service.AdminService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("api/v1/admin")
public class AdminController {
    final AdminService service ;
    AdminController( AdminService service){
        this.service = service;
    }
    @PostMapping("/baneUser")
    public ResponseEntity<AdminErrorDto> BaneUser(@RequestBody AdminErrorDto entity) {
        AdminErrorDto response = service.BannedUser(entity);

        
        return ResponseEntity.status(response.getCode()).body(response);
    }
       @PostMapping("/deletPost")
    public ResponseEntity<AdminErrorDto> DeletePost(@RequestBody AdminErrorDto entity) {
        AdminErrorDto response = service.DeletePost(entity);

        
        return ResponseEntity.status(response.getCode()).body(response);
    }
    
    
}
