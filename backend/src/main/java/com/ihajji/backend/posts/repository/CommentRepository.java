package com.ihajji.backend.posts.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ihajji.backend.posts.entity.CommentEntity;

public interface CommentRepository extends JpaRepository<CommentEntity, Long>{
    List<CommentEntity> findByPostId(Long id);
    
}
