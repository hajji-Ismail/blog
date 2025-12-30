package com.ihajji.backend.posts.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ihajji.backend.posts.dto.ErrorDto;
import com.ihajji.backend.posts.dto.PostErrorsDto;
import com.ihajji.backend.posts.dto.PostFeedDto;
import com.ihajji.backend.posts.dto.PostRequestDTO;
import com.ihajji.backend.posts.service.PostService;

import com.ihajji.backend.user.config.UserPrincipal;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;






@RestController
@RequestMapping("api/v1/user/Post")
public class PostController {
      private final PostService PostService;
      PostController(PostService PostService){
        this.PostService = PostService;
      }
    @PostMapping("save")
    public ResponseEntity<PostErrorsDto> save(@ModelAttribute PostRequestDTO dto, @AuthenticationPrincipal UserPrincipal principal) {
      
        PostErrorsDto post = this.PostService.SavePost(dto, principal.getUsername());
        
      
        
        return ResponseEntity.status(post.getCode()).body(post);
        
    }
    @GetMapping("/load")
    public ResponseEntity<List<PostFeedDto>> Load() {
      List<PostFeedDto> Post =this.PostService.getAllPosts();
      return ResponseEntity.ok(Post);
       
    }
    @PostMapping("/delete")
    public ResponseEntity< ErrorDto> delete(@RequestBody Long  postId ,  @AuthenticationPrincipal UserPrincipal principal) {
      ErrorDto data = this.PostService.delete(principal.getUsername(), postId);
    
        return ResponseEntity.status(data.getCode()).body(data);
    }
    
 
    
   
}
