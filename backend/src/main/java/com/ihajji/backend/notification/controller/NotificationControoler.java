package com.ihajji.backend.notification.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ihajji.backend.notification.dto.NotificationErrDto;
import com.ihajji.backend.notification.services.NotificationServices;
import com.ihajji.backend.user.config.UserPrincipal;

@RestController
@RequestMapping("api/v1/user/notification")
public class NotificationControoler {
    final NotificationServices service;
    NotificationControoler(NotificationServices service){
        this.service =  service;
    }
    @GetMapping("/load")
    public ResponseEntity<NotificationErrDto> Load(@AuthenticationPrincipal UserPrincipal principal){
        NotificationErrDto response = service.load(principal.getUsername()) ;
        return ResponseEntity.status(response.getCode()).body(response);

    }

}
