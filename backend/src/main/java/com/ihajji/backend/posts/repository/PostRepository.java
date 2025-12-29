package com.ihajji.backend.posts.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ihajji.backend.posts.dto.PostFeedDto;
import com.ihajji.backend.posts.entity.PostEntity;

@Repository
public interface PostRepository extends JpaRepository< PostEntity, Long>{


  @Query("""
SELECT new com.ihajji.backend.posts.dto.PostFeedDto(
    p.id, p.title, p.content, p.user.username,
    SIZE(p.reactions), SIZE(p.comments), p.createdAt
)
FROM PostEntity p
ORDER BY p.createdAt DESC
""")
List<PostFeedDto> findAllPostFeed();

}


    
