package com.ihajji.backend.posts.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;

import com.ihajji.backend.user.entity.UserEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "Posts")
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReactionEntity> reactions;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // This creates the foreign key column
    private UserEntity user;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;
@OneToMany(
    mappedBy = "post",
    cascade = CascadeType.ALL,
    orphanRemoval = true,
    fetch = FetchType.LAZY
)
private Set<CommentEntity> comments = new HashSet<>();

@OneToMany(
    mappedBy = "post",
    cascade = CascadeType.ALL,
    orphanRemoval = true,
    fetch = FetchType.LAZY
)
private Set<MediaEntity> medias = new HashSet<>();

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public String getContent() {
        return content;
    }

    public Long getId() {
        return id;
    }

    public List<ReactionEntity> getReactions() {
        return reactions;
    }

    public String getTitle() {
        return title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setReactions(List<ReactionEntity> reactions) {
        this.reactions = reactions;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Set<MediaEntity> getMedias() {
        return medias;
    }

public void setMedias(Set<MediaEntity> medias) {
    this.medias = medias;
}

 public Set<CommentEntity> getComments() {
     return comments;
 }
 public void setComments(Set<CommentEntity> comments) {
     this.comments = comments;
 }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Transient
    public Integer getCommentCount() {
        return comments == null ? 0 : comments.size();
    }

    @Transient
    public Integer getReactionCount() {
        return reactions == null ? 0 : reactions.size();
    }

}
