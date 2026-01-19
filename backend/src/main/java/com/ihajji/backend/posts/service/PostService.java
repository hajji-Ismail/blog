package com.ihajji.backend.posts.service;


import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ihajji.backend.notification.services.NotificationServices;
import com.ihajji.backend.posts.dto.ErrorDto;
import com.ihajji.backend.posts.dto.PostErrorsDto;
import com.ihajji.backend.posts.dto.PostFeedResponse;
import com.ihajji.backend.posts.dto.PostRequestDTO;
import com.ihajji.backend.posts.entity.MediaEntity;
import com.ihajji.backend.posts.entity.PostEntity;
import com.ihajji.backend.posts.repository.PostRepository;
import com.ihajji.backend.posts.repository.ReactionRepository;
import com.ihajji.backend.user.entity.UserEntity;
import com.ihajji.backend.user.repository.UserRepository;
import com.ihajji.backend.user.utils.FileUploadService;


@Service
public class PostService {
    private final FileUploadService fileUploadService;
    private final UserRepository UserRepository;
    private final PostRepository PostRepo;
    private final ReactionRepository Reactionrepo;
    private final NotificationServices notification;

    public PostService(FileUploadService fileUploadService, UserRepository UserRepository, PostRepository PostRepo , ReactionRepository Reactionrepo, NotificationServices notification) {
        this.fileUploadService = fileUploadService;
        this.UserRepository = UserRepository;
        this.PostRepo = PostRepo;
        this.Reactionrepo = Reactionrepo;
        this.notification = notification;
        

    }
   public PostErrorsDto savePost(PostRequestDTO dto, String username) {


    PostErrorsDto errors = new PostErrorsDto();

    if (dto.title() == null || dto.title().isBlank() || dto.title().length() > 50) {
        errors.setCode(HttpStatus.SC_BAD_REQUEST);
        errors.setTitle("The title cannot be empty or exceed 50 characters.");
    }

    if (dto.content() == null || dto.content().isBlank() || dto.content().length() > 100_000) {
        errors.setCode(HttpStatus.SC_BAD_REQUEST);
        errors.setContent("The content cannot be empty or exceed 100000 characters.");
    }

    if (errors.getCode() != HttpStatus.SC_OK) {
        errors.setMessage("Please revise your input fields.");
        return errors;
    }

    UserEntity user = UserRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalStateException("User not found"));

    PostEntity post = new PostEntity();
    post.setTitle(dto.title());
    post.setContent(dto.content());
    post.setUser(user);

    Set<MediaEntity> mediaEntities = new HashSet<>();

    if (dto.mediaFiles() != null) {
        for (MultipartFile file : dto.mediaFiles()) {
            try {
                String url = fileUploadService.uploadFile(file, "post-media");

                MediaEntity media = new MediaEntity();
                media.setMedia(url);
                media.setPost(post);      
                mediaEntities.add(media); 
            

            } catch (IOException e) {
                errors.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                errors.setMessage("Something went wrong. Please try again later.");
                return errors;
            }
        }
    }

    post.setMedias(mediaEntities);

     PostRepo.save(post);
this.notification.SavePosts(user)   ;

    return errors;
}
   @Transactional(readOnly = true)
    public List<PostFeedResponse> getPostFeed(String username ) {
        List<PostEntity> posts = this.PostRepo.findAllPostsWithUser();
     Optional<UserEntity> user = this.UserRepository.findByUsername(username);
        if (!user.isPresent()) {
            return null ; 
        }
        
        return posts.stream().map(post -> new PostFeedResponse(
            post.getId(),
            post.getTitle(),
            post.getContent(),
            post.getUser() != null ? post.getUser().getUsername() : "Anonymous",
            post.getUser() != null ? post.getUser().getProfileImageUrl() : null,
            post.getReactions().size(),
            post.getComments().size(),
            post.getCreatedAt(),
           
            post.getMedias().stream().map(m -> m.getMedia()).toList(),
            this.Reactionrepo.existsByUserIdAndPostId(user.get().getId(), post.getId())
                

        )).toList();
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
    public List<PostFeedResponse> getPostbyUsername(String username ){
        Optional<UserEntity> user = this.UserRepository.findByUsername(username);
        if (!user.isPresent()) {
            return null ; 
        }
        List<PostEntity> posts = this.PostRepo.findAByUser(user.get());
 return posts.stream().map(post -> new PostFeedResponse(
            post.getId(),
            post.getTitle(),
            post.getContent(),
            post.getUser() != null ? post.getUser().getUsername() : "Anonymous",
            post.getUser() != null ? post.getUser().getProfileImageUrl() : null,
            post.getReactions().size(),
            post.getComments().size(),
            post.getCreatedAt(),
           
            post.getMedias().stream().map(m -> m.getMedia()).toList(),
            this.Reactionrepo.existsByUserIdAndPostId(user.get().getId(), post.getId())
                

        )).toList();
    }

    
}
