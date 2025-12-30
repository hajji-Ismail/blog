package com.ihajji.backend.posts.service;

import java.util.List;
import java.util.Optional;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.stereotype.Service;

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
    final CommentRepository repo;
    final UserRepository userRepo;
    final PostRepository postrepo;

    CommentsService(CommentRepository repo, UserRepository user, PostRepository postrepo) {
        this.repo = repo;
        this.userRepo = user;
        this.postrepo = postrepo;

    }

    public ErrorDto Save(CommentSaveDto dto) {
       


        if (dto.getContent().length() > 10000) {
            return new ErrorDto(HttpStatus.SC_BAD_REQUEST, "your comments should be no more than 10000");

        }
        Optional<UserEntity> user = userRepo.findByUsername(dto.getUsername());
        if (!user.isPresent()) {
            return new ErrorDto(HttpStatus.SC_INTERNAL_SERVER_ERROR, "something wznt wrong go back later");
        }
        Optional<PostEntity> post = postrepo.findById(dto.getPostId());
        if (!post.isPresent()) {
            return new ErrorDto(HttpStatus.SC_INTERNAL_SERVER_ERROR, "something wznt wrong go back later");

        }
        CommentEntity comment = new CommentEntity();
        comment.setContent(dto.getContent());
        comment.setUser(user.get());
        comment.setPost(post.get());
        repo.save(comment);

        return new ErrorDto();

    }
    public List<CommentEntity> Load(Long id){
        
        return this.repo.findByPostId(id);
    }
}
