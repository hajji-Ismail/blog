package com.ihajji.backend.posts.dto;

import java.time.LocalDateTime;

public class PostFeedDto {

       private Long id;
    private String title;
    private String content;
    private String username;
    private long reactionsCount;
    private long commentsCount;
    private LocalDateTime createdAt;

    public PostFeedDto(Long id, String title, String content, String username,
                       long reactionsCount, long commentsCount, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.username = username;
        this.reactionsCount = reactionsCount;
        this.commentsCount = commentsCount;
        this.createdAt = createdAt;
    }
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
   
public long getCommentsCount() {
    return commentsCount;
}
public LocalDateTime getCreatedAt() {
    return createdAt;
}
public long getReactionsCount() {
    return reactionsCount;
}
public String getUsername() {
    return username;
}
    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }
    
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
  public void setCommentsCount(long commentsCount) {
      this.commentsCount = commentsCount;
  }
  public void setReactionsCount(long reactionsCount) {
      this.reactionsCount = reactionsCount;
  }
  public void setUsername(String username) {
      this.username = username;
  }
  
}
