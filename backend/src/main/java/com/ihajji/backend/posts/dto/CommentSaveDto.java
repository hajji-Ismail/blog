package com.ihajji.backend.posts.dto;


public class CommentSaveDto {
    private Long postId ;
    private String username;
    private String content ;
    public CommentSaveDto(){

    }
    public String getContent() {
        return content;
    }
   public Long getPostId() {
       return postId;
   }
  
   public String getUsername() {
       return username;
   }
    public void setContent(String content) {
        this.content = content;
    }
  public void setPostId(Long postId) {
      this.postId = postId;
  }
   
    public void setUsername(String username) {
        this.username = username;
    }

    
}