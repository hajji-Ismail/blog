package com.ihajji.backend.posts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ihajji.backend.posts.entity.PostEntity;

@Repository
public interface PostRepository extends JpaRepository< PostEntity, Long>{

    
}