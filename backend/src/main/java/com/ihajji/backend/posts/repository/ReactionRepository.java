package com.ihajji.backend.posts.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ihajji.backend.posts.entity.ReactionEntity;

public interface ReactionRepository extends JpaRepository<ReactionEntity, Long> {

    boolean existsByUserIdAndPostId(Long userId, Long postId);

    Optional<ReactionEntity> findByUserIdAndPostId(Long userId, Long postId);

    void deleteByUserIdAndPostId(Long userId, Long postId);
}
