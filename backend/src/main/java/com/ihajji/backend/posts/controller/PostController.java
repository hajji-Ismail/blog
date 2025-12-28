package com.ihajji.backend.posts.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ihajji.backend.posts.dto.PostResponseDTO;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("api/v1/Post")
public class PostController {
    @PostMapping("/Save")
    public PostResponseDTO postMethodName(@RequestBody PostResponseDTO entity) {
       
        
        return entity;
    }
    
    
}
