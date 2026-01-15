package com.ihajji.backend.posts.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ihajji.backend.posts.dto.ErrorDto;
import com.ihajji.backend.posts.service.ReactionService;
import com.ihajji.backend.user.config.UserPrincipal;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("api/v1/user/post")
public class ReactionController {
    final ReactionService react;

    ReactionController(ReactionService react) {
        this.react = react;
    }

    @PostMapping("/react")
    public ResponseEntity<ErrorDto> postMethodName(@RequestBody Long postId,
            @AuthenticationPrincipal UserPrincipal principal) {
        ErrorDto data = this.react.react(principal.getUsername(), postId);

        return ResponseEntity.status(data.getCode()).body(data);
    }

}
