package com.ihajji.backend.posts.service;

import java.util.Optional;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ihajji.backend.posts.dto.ErrorDto;
import com.ihajji.backend.posts.entity.PostEntity;
import com.ihajji.backend.posts.entity.ReactionEntity;
import com.ihajji.backend.posts.repository.PostRepository;
import com.ihajji.backend.posts.repository.ReactionRepository;
import com.ihajji.backend.user.entity.UserEntity;
import com.ihajji.backend.user.repository.UserRepository;

@Service
public class ReactionService {
    final ReactionRepository repo;
    final UserRepository urepo;
    final PostRepository prepo;

    ReactionService(ReactionRepository repo, UserRepository urepo, final PostRepository prepo) {
        this.repo = repo;
        this.urepo = urepo;
        this.prepo = prepo;

    }

    public ErrorDto react(String username, Long postid) {
        Optional<UserEntity> user = this.urepo.findByUsername(username);
        if (!user.isPresent()) {
            return new ErrorDto(HttpStatus.SC_INTERNAL_SERVER_ERROR, "someting went wrong please come back later");
        }
        if (this.repo.existsByUserIdAndPostId(user.get().getId(), postid)) {
            Optional<ReactionEntity> reaction = this.repo.findByUserIdAndPostId(user.get().getId(), postid);
            if (!reaction.isPresent()) {
                return new ErrorDto(HttpStatus.SC_INTERNAL_SERVER_ERROR, "someting went wrong please come back later");

            }

            this.repo.delete(reaction.get());
            return new ErrorDto();
        }
        ReactionEntity react = new ReactionEntity();
        react.setUser(user.get());
        Optional<PostEntity> post = this.prepo.findById(postid);
        if (!post.isPresent()) {
            return new ErrorDto(HttpStatus.SC_BAD_REQUEST, "the post does not exist");

        }
        react.setPost(post.get());

        this.repo.save(react);

return new ErrorDto();
    }

}
