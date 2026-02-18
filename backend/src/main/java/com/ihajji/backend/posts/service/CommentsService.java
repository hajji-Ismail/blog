package com.ihajji.backend.posts.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ihajji.backend.posts.dto.CommentDto;
import com.ihajji.backend.posts.dto.CommentEditDto;
import com.ihajji.backend.posts.dto.CommentSaveDto;
import com.ihajji.backend.posts.dto.ErrorDto;
import com.ihajji.backend.posts.entity.CommentEntity;
import com.ihajji.backend.posts.entity.PostEntity;
import com.ihajji.backend.posts.repository.CommentRepository;
import com.ihajji.backend.posts.repository.PostRepository;
import com.ihajji.backend.user.entity.UserEntity;
import com.ihajji.backend.user.repository.UserRepository;

@Service
public class CommentsService {

    private final CommentRepository repo;
    private final UserRepository userRepo;
    private final PostRepository postRepo;

    public CommentsService(CommentRepository repo, UserRepository userRepo, PostRepository postRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
        this.postRepo = postRepo;
    }

    public ErrorDto save(CommentSaveDto dto) {
        if (dto.getContent() == null || dto.getContent().isBlank()) {
            return new ErrorDto(HttpStatus.SC_BAD_REQUEST, "Comment content cannot be empty");
        }
        if (dto.getContent().length() > 10000) {
            return new ErrorDto(HttpStatus.SC_BAD_REQUEST, "Comment cannot exceed 10000 characters");
        }

        Optional<UserEntity> userOpt = userRepo.findByUsername(dto.getUsername());
        if (userOpt.isEmpty()) {
            return new ErrorDto(HttpStatus.SC_INTERNAL_SERVER_ERROR, "User not found");
        }

        Optional<PostEntity> postOpt = postRepo.findById(dto.getPostId());
        if (postOpt.isEmpty()) {
            return new ErrorDto(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Post not found");
        }

        CommentEntity comment = new CommentEntity();
        comment.setContent(dto.getContent());
        comment.setUser(userOpt.get());
        comment.setPost(postOpt.get());
        repo.save(comment);

        return new ErrorDto();
    }

    public List<CommentDto> load(Long postId) {
        return repo.findByPostId(postId).stream()
            .map(c -> new CommentDto(
                c.getId(),
                c.getPost().getId(),
                c.getUser().getId(),
                c.getContent(),
                c.getCreatedAt(),
                c.getUser().getUsername(),
                c.getUser().getProfileImageUrl()
            ))
            .collect(Collectors.toList());
    }

    public ErrorDto edit(String username, CommentEditDto dto) {
        if (dto.getId() == null) {
            return new ErrorDto(HttpStatus.SC_BAD_REQUEST, "Comment id is required");
        }
        if (dto.getContent() == null || dto.getContent().isBlank()) {
            return new ErrorDto(HttpStatus.SC_BAD_REQUEST, "Comment content cannot be empty");
        }
        if (dto.getContent().length() > 10000) {
            return new ErrorDto(HttpStatus.SC_BAD_REQUEST, "Comment cannot exceed 10000 characters");
        }

        Optional<CommentEntity> commentOpt = repo.findById(dto.getId());
        if (commentOpt.isEmpty()) {
            return new ErrorDto(HttpStatus.SC_BAD_REQUEST, "Comment not found");
        }

        CommentEntity comment = commentOpt.get();
        if (!comment.getUser().getUsername().equals(username)) {
            return new ErrorDto(HttpStatus.SC_BAD_REQUEST, "Only the owner can edit this comment");
        }

        comment.setContent(dto.getContent());
        repo.save(comment);
        return new ErrorDto();
    }

    public ErrorDto delete(String username, Long commentId) {
        if (commentId == null) {
            return new ErrorDto(HttpStatus.SC_BAD_REQUEST, "Comment id is required");
        }

        Optional<CommentEntity> commentOpt = repo.findById(commentId);
        if (commentOpt.isEmpty()) {
            return new ErrorDto(HttpStatus.SC_BAD_REQUEST, "Comment not found");
        }

        CommentEntity comment = commentOpt.get();
        if (!comment.getUser().getUsername().equals(username)) {
            return new ErrorDto(HttpStatus.SC_BAD_REQUEST, "Only the owner can delete this comment");
        }

        repo.delete(comment);
        return new ErrorDto();
    }
}
