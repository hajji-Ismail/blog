package com.ihajji.backend.posts.dto;

import org.apache.hc.core5.http.HttpStatus;

import com.ihajji.backend.posts.entity.PostEntity;

public class PostErrorsDto {
    private int Code ;
    private String Message;
    private String title;
    private String content;
    private PostEntity post;
    
    public PostErrorsDto(){
        this.Code = HttpStatus.SC_OK;
        this.Message = "this ops went smouthly";
    }
    public int getCode() {
        return Code;
    }
    public PostEntity getPost() {
        return post;
    }
    public void setPost(PostEntity post) {
        this.post = post;
    }
    public String getContent() {
        return content;
    }
    public String getMessage() {
        return Message;
    }
    public String getTitle() {
        return title;
    }
    public void setCode(int code) {
        Code = code;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public void setMessage(String message) {
        Message = message;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    
}
