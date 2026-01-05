package com.ihajji.backend.reports.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ihajji.backend.reports.dto.ReportDto;
import com.ihajji.backend.reports.service.ReportService;
import com.ihajji.backend.user.config.UserPrincipal;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("api/v1/user/report")
public class ReportsController {
    final ReportService service;
    ReportsController(ReportService service){
        this.service = service;
        
    }
    @PostMapping("/post")
    public ResponseEntity<ReportDto> ReportPost(@RequestBody ReportDto dto, @AuthenticationPrincipal UserPrincipal principal) {
        ReportDto response = service.reportPost(principal.getUsername(), dto);
        return ResponseEntity.status(response.getCode()).body(response);
    }
    @PostMapping("/user")
    public ResponseEntity<ReportDto> ReportUser(@RequestBody ReportDto dto, @AuthenticationPrincipal UserPrincipal principal) {
        ReportDto response = service.ReportUser(principal.getUsername(), dto);
        return ResponseEntity.status(response.getCode()).body(response);
    }
    
    
    
}
