package com.ihajji.backend.posts.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ihajji.backend.posts.entity.PostEntity;
import com.ihajji.backend.user.entity.UserEntity;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {
    // We fetch the posts and the users in one single query
 @Query("""
    SELECT p 
    FROM PostEntity p
    JOIN FETCH p.user u
    WHERE u.id IN (
        SELECT pr.following.id 
        FROM ProfileEntity pr 
        WHERE pr.follower.id = :userId
    )
    ORDER BY p.createdAt DESC
""")
List<PostEntity> findPostsFromFollowedUsers(@Param("userId") Long userId);

    List<PostEntity> findAByUser(UserEntity user);
}
