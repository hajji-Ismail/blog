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

    public PostService(FileUploadService fileUploadService, UserRepository UserRepository, PostRepository PostRepo,
            ReactionRepository Reactionrepo, NotificationServices notification) {
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
            return errors;
        }

        if (dto.content() == null || dto.content().isBlank() || dto.content().length() > 100_00) {
            errors.setCode(HttpStatus.SC_BAD_REQUEST);
            errors.setContent("The content cannot be empty or exceed 10000 characters.");
            return errors;
        }

        if (errors.getCode() != HttpStatus.SC_OK) {
            errors.setMessage("Please revise your input fields.");
            return errors;
        }
        if (dto.mediaFiles() != null && dto.mediaFiles().size() > 5) {
            errors.setCode(HttpStatus.SC_BAD_REQUEST);
            errors.setMessage("Maximum 5 files allowed.");
            return errors;

        }

        Optional<UserEntity> user = UserRepository.findByUsername(username);
        if (!user.isPresent()) {
            errors.setCode(HttpStatus.SC_BAD_REQUEST);
            errors.setMessage("user Not Found");
            return errors;
        }
        if (user.get().getIs_baned()) {
            errors.setCode(HttpStatus.SC_UNAUTHORIZED);
            errors.setMessage("user is banned");
            return errors;
        }

        PostEntity post = new PostEntity();
        post.setTitle(dto.title());
        post.setContent(dto.content());
        post.setUser(user.get());

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
        this.notification.SavePosts(user.get());

        return errors;
    }

    @Transactional(readOnly = true)
    public List<PostFeedResponse> getPostFeed(String username) {
        Optional<UserEntity> user = this.UserRepository.findByUsername(username);
        if (!user.isPresent()) {
            return null;
        }
        if (user.get().getIs_baned()) {
            return null;
        }
        List<PostEntity> posts = this.PostRepo.findPostsFromFollowedUsers(user.get().getId());

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

    public ErrorDto delete(String username, Long PostId) {
        Optional<UserEntity> user = this.UserRepository.findByUsername(username);
        if (!user.isPresent()) {
            return new ErrorDto(HttpStatus.SC_BAD_REQUEST, "user not found");
        }
        if (user.get().getIs_baned()) {
            return new ErrorDto(HttpStatus.SC_UNAUTHORIZED, "user not found");
            
        }
        Optional<PostEntity> post = this.PostRepo.findById(PostId);
        if (!post.isPresent()) {
            return new ErrorDto(HttpStatus.SC_BAD_REQUEST, "the Post does not existe");
        }
        if (!post.get().getUser().getUsername().equals(username)) {
            return new ErrorDto(HttpStatus.SC_BAD_REQUEST, "the post can be deleted by admin or the owner only");

        }
        this.PostRepo.delete(post.get());

        return new ErrorDto();

    }

    public ErrorDto edit(PostRequestDTO dto, String username) {
        Optional<UserEntity> user = this.UserRepository.findByUsername(username);

        if (!user.isPresent()) {
            return new ErrorDto(HttpStatus.SC_BAD_REQUEST, "user not found");
        }
        if (user.get().getIs_baned()) {
            return new ErrorDto(HttpStatus.SC_UNAUTHORIZED, "user not found");
            
        }
        Optional<PostEntity> post = this.PostRepo.findById(dto.ID());
        if (!post.isPresent()) {
            return new ErrorDto(HttpStatus.SC_BAD_REQUEST, "the Post does not existe");
        }
        if (!post.get().getUser().getUsername().equals(username)) {
            return new ErrorDto(HttpStatus.SC_BAD_REQUEST, "the post can be edited the owner only");
        }
        if (dto.title() == null || dto.title().isBlank() || dto.title().length() > 50) {
            return new ErrorDto(HttpStatus.SC_BAD_REQUEST, "The title cannot be empty or exceed 50 characters.");
        }
        if (dto.content() == null || dto.content().isBlank() || dto.content().length() > 100_000) {
            return new ErrorDto(HttpStatus.SC_BAD_REQUEST, "The content cannot be empty or exceed 100000 characters.");
        }

        PostEntity editedPost = post.get();
        editedPost.setTitle(dto.title());
        editedPost.setContent(dto.content());

        boolean hasNewMedia = dto.mediaFiles() != null
                && dto.mediaFiles().stream().anyMatch(file -> file != null && !file.isEmpty());

        if (hasNewMedia) {
            Set<MediaEntity> mediaEntities = new HashSet<>();

            for (MultipartFile file : dto.mediaFiles()) {
                if (file == null || file.isEmpty()) {
                    continue;
                }
                try {
                    String url = fileUploadService.uploadFile(file, "post-media");
                    MediaEntity media = new MediaEntity();
                    media.setMedia(url);
                    media.setPost(editedPost);
                    mediaEntities.add(media);
                } catch (IOException e) {
                    return new ErrorDto(HttpStatus.SC_INTERNAL_SERVER_ERROR,
                            "Something went wrong. Please try again later.");
                }
            }

            editedPost.getMedias().clear();
            editedPost.getMedias().addAll(mediaEntities);
        }

        this.PostRepo.save(editedPost);

        return new ErrorDto();
    }
public List<PostFeedResponse> getPostbyUsername(String username ){
        Optional<UserEntity> user = this.UserRepository.findByUsername(username);
        if (!user.isPresent()) {
            return null ; 
        }
        if (user.get().getIs_baned()){
            return null;
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
