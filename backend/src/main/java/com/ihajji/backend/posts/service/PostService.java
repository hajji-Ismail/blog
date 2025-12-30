package com.ihajji.backend.posts.service;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ihajji.backend.posts.dto.ErrorDto;
import com.ihajji.backend.posts.dto.PostErrorsDto;
import com.ihajji.backend.posts.dto.PostFeedDto;
import com.ihajji.backend.posts.dto.PostRequestDTO;
import com.ihajji.backend.posts.entity.MediaEntity;
import com.ihajji.backend.posts.entity.PostEntity;
import com.ihajji.backend.posts.repository.PostRepository;
import com.ihajji.backend.user.entity.UserEntity;
import com.ihajji.backend.user.repository.UserRepository;
import com.ihajji.backend.user.utils.FileUploadService;


@Service
public class PostService {
    private final FileUploadService fileUploadService;
    private final UserRepository UserRepository;
    private final PostRepository PostRepo;

    public PostService(FileUploadService fileUploadService, UserRepository UserRepository, PostRepository PostRepo) {
        this.fileUploadService = fileUploadService;
        this.UserRepository = UserRepository;
        this.PostRepo = PostRepo;

    }
    public PostErrorsDto SavePost(PostRequestDTO dto , String username) {
        PostErrorsDto errors = new PostErrorsDto();
        if (dto.title().length() > 50 || dto.title().isEmpty() ){
            errors.setCode(HttpStatus.SC_BAD_REQUEST);
            errors.setMessage("revise your content you may have a probleme in on ar multiple feilds");
            errors.setTitle("the tile cannot be empty or more than 50 charachter wide");

        }
         if (dto.content().length() > 100000 || dto.content().isEmpty() ){
            errors.setCode(HttpStatus.SC_BAD_REQUEST);
            errors.setMessage("revise your content you may have a probleme in on ar multiple feilds");
            errors.setContent("the Content cannot be empty or more than 100000 charachter wide");

        }
        if (errors.getCode()!=HttpStatus.SC_OK){
            return errors;
        }
Optional< UserEntity> User = this.UserRepository.findByUsername(username);
if (!User.isPresent()){
    errors.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    errors.setMessage("sorry try later pleas");
    return errors;
    
}
PostEntity post = new PostEntity();
post.setTitle(dto.title());
post.setContent(dto.content());
post.setUser(User.get());
List<MediaEntity> mediaEntities = new ArrayList<>();
if (dto.mediaFiles() != null) {
        for (MultipartFile file : dto.mediaFiles()) {
            try {
                String url = fileUploadService.uploadFile(file, "post-media");
                
                MediaEntity media = new MediaEntity();
                media.setMedia(url);
                media.setPost(post);
                media.setCreatedAt(LocalDateTime.now());
                mediaEntities.add(media);
            } catch (IOException e) {
                errors.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                errors.setMessage("sommething went wrong come back later");
                return errors ;
            }

        }
       

    }
     post.setMedias(mediaEntities);
     PostEntity postEntity = PostRepo.save(post);
     errors.setPost(postEntity);
      

        return errors;
        
    }
    @Transactional(readOnly = true)
    public List<PostFeedDto> getAllPosts() {

        return this.PostRepo.findAllPostFeed();
    }
    public ErrorDto delete(String username , Long PostId ){
        Optional<PostEntity> post = this.PostRepo.findById(PostId);
        if (!post.isPresent()){
            return new ErrorDto(HttpStatus.SC_BAD_REQUEST, "the Post does not existe");
        }
        if (!post.get().getUser().getUsername().equals(username)){
                        return new ErrorDto(HttpStatus.SC_BAD_REQUEST, "the post can be deleted by admin or the owner only");

        }
        this.PostRepo.delete(post.get());
        
        return new ErrorDto();

    } 
    public ErrorDto edit(PostRequestDTO dto , String username){
        Optional<PostEntity> post = this.PostRepo.findById(dto.ID());
               if (!post.isPresent()){
            return new ErrorDto(HttpStatus.SC_BAD_REQUEST, "the Post does not existe");
        }
          if (!post.get().getUser().getUsername().equals(username)){
                        return new ErrorDto(HttpStatus.SC_BAD_REQUEST, "the post can be edited the owner only");

        }
        PostEntity EditedPost = new PostEntity();
        EditedPost.setContent(dto.content());
        EditedPost.setTitle(dto.title());
        EditedPost.setId(dto.ID());

        this.PostRepo.save(EditedPost);

        return new ErrorDto();
    }

    
}
