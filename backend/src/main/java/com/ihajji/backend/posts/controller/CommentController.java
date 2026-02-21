package com.ihajji.backend.posts.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ihajji.backend.posts.dto.CommentDto;
import com.ihajji.backend.posts.dto.CommentEditDto;
import com.ihajji.backend.posts.dto.CommentSaveDto;
import com.ihajji.backend.posts.dto.ErrorDto;
import com.ihajji.backend.posts.service.CommentsService;
import com.ihajji.backend.user.config.UserPrincipal;

@RestController
@RequestMapping("/api/v1/user/comment")
public class CommentController {

    private final CommentsService service;

    public CommentController(CommentsService service) {
        this.service = service;
    }

    @PostMapping("/save")
    public ResponseEntity<ErrorDto> saveComment(@RequestBody CommentSaveDto dto,
                                                @AuthenticationPrincipal UserPrincipal principal) {
        dto.setUsername(principal.getUsername());
        ErrorDto data = this.service.save(dto);
        return ResponseEntity.status(data.getCode()).body(data);
    }

    @GetMapping("/load")
    public ResponseEntity<List<CommentDto>> load(@RequestParam Long param,@AuthenticationPrincipal UserPrincipal principal) {
        List<CommentDto> comments = this.service.load(param);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/edit")
    public ResponseEntity<ErrorDto> editComment(@RequestBody CommentEditDto dto,
                                                @AuthenticationPrincipal UserPrincipal principal) {
        ErrorDto data = this.service.edit(principal.getUsername(), dto);
        return ResponseEntity.status(data.getCode()).body(data);
    }

    @PostMapping("/delete")
    public ResponseEntity<ErrorDto> deleteComment(@RequestBody Long commentId,
                                                  @AuthenticationPrincipal UserPrincipal principal) {
        ErrorDto data = this.service.delete(principal.getUsername(), commentId);
        return ResponseEntity.status(data.getCode()).body(data);
    }
}
