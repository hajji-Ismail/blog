package com.ihajji.backend.posts.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import com.ihajji.backend.user.entity.UserEntity;

import jakarta.persistence.*;


@Entity
public class ReactionEntity {
   @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
     @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // This creates the foreign key column
    private UserEntity user;
      @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false) // This creates the foreign key column
    private PostEntity post;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public Long getId() {
        return id;
    }
    public PostEntity getPost() {
        return post;
    }
    public UserEntity getUser() {
        return user;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setPost(PostEntity post) {
        this.post = post;
    }
    public void setUser(UserEntity user) {
        this.user = user;
    }
}