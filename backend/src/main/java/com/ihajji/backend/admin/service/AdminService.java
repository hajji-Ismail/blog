package com.ihajji.backend.admin.service;

import java.util.Optional;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ihajji.backend.admin.dto.AdminErrorDto;
import com.ihajji.backend.posts.entity.PostEntity;
import com.ihajji.backend.posts.repository.PostRepository;
import com.ihajji.backend.user.entity.UserEntity;
import com.ihajji.backend.user.repository.UserRepository;

@Service
public class AdminService {
    final UserRepository userrepo;
    final PostRepository postrepo;
    AdminService(UserRepository userrepo,PostRepository postrepo){
        this.postrepo= postrepo;
        this.userrepo = userrepo;
    }
    public AdminErrorDto BannedUser(AdminErrorDto dto){
        Optional<UserEntity> user =this.userrepo.findByUsername(dto.getUsername());
        if (!user.isPresent()){
            return new AdminErrorDto(HttpStatus.SC_BAD_REQUEST, "the user can't be found");

        }
        user.get().setIs_baned(true);
        userrepo.save(user.get());
        return new AdminErrorDto();


    } 
       public AdminErrorDto DeletePost(AdminErrorDto dto){
        Optional<PostEntity> post =this.postrepo.findById(dto.getPost_id());
        if (!post.isPresent()){
            return new AdminErrorDto(HttpStatus.SC_BAD_REQUEST, "the user can't be found");

        }
        postrepo.delete(post.get());
        return new AdminErrorDto();


    } 
    
}
