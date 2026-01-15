package com.ihajji.backend.posts.entity;



import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.*;


@Entity
@Table(name = "Media") // Good practice to name the table
public class MediaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false) // The DB column name
    private PostEntity post;

    @Column(name = "media")
    private String media; 
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public PostEntity getPost() { return post; }
    public void setPost(PostEntity post) { this.post = post; }

    public String getMedia() { return media; }
    public void setMedia(String media) { this.media = media; }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
}