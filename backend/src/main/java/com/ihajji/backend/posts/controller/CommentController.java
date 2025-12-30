package com.ihajji.backend.posts.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ihajji.backend.posts.dto.CommentSaveDto;
import com.ihajji.backend.posts.dto.ErrorDto;
import com.ihajji.backend.posts.service.CommentsService;
import com.ihajji.backend.user.config.UserPrincipal;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("api/v1/user/comment")
public class CommentController {
    final CommentsService service;
    CommentController(CommentsService service){
        this.service = service;
    }
    @PostMapping("/save")
    public ResponseEntity<ErrorDto> SaveComment(@RequestBody CommentSaveDto dto ,@AuthenticationPrincipal UserPrincipal principal) {
        dto.setUsername(principal.getUsername());
        ErrorDto data = this.service.Save(dto); 

        return ResponseEntity.status(data.getCode()).body(data);
    }
    @GetMapping("/load")
    public ResponseEntity<ErrorDto> load (@RequestParam Long param) {
        ErrorDto error = new ErrorDto();
    error.setComments(this.service.Load(param)); 
    return ResponseEntity.ok(error);
       
    }
    

    
}
