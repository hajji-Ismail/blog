package com.ihajji.backend.posts.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import com.ihajji.backend.user.entity.UserEntity;

import jakarta.persistence.*;

@Entity

public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false) // The DB column name
    private PostEntity post;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // The DB column name
    private UserEntity user;
    @Column(name = "Content")
    private String Content;
   

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    public String getContent() {
        return Content;
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
    public void setContent(String content) {
        Content = content;
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
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
