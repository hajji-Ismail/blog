package com.ihajji.backend.posts.dto;

import java.util.ArrayList;
import java.util.List;

import org.apache.hc.core5.http.HttpStatus;

import com.ihajji.backend.posts.entity.CommentEntity;

public class ErrorDto {
    private int code;
    private String Message;
    private List<CommentEntity> comments;
    public ErrorDto(){
        this.Message = "this ops went smouthly";
        this.code  = HttpStatus.SC_OK;
        this.comments = new ArrayList<CommentEntity>();
    }
     public ErrorDto(int code ,String Message ){
        this.Message = Message;
        this.code  = code;
    }

    public int getCode() {
        return code;
    }
    public List<CommentEntity> getComments() {
        return comments;
    }
    public String getMessage() {
        return Message;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public void setMessage(String message) {
        Message = message;
    }
public void setComments(List<CommentEntity> comments) {
    this.comments = comments;
}
    
}