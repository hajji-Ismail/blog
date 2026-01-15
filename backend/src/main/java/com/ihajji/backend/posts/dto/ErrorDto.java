package com.ihajji.backend.posts.dto;

import java.util.ArrayList;
import java.util.List;

import org.apache.hc.core5.http.HttpStatus;

public class ErrorDto {
    private int code;
    private String message;
    private List<CommentDto> comments;

    public ErrorDto() {
        this.message = "Operation completed successfully";
        this.code = HttpStatus.SC_OK;
        this.comments = new ArrayList<>();
    }

    public ErrorDto(int code, String message) {
        this.code = code;
        this.message = message;
        this.comments = new ArrayList<>();
    }

    // getters & setters
    public int getCode() { return code; }
    public String getMessage() { return message; }
    public List<CommentDto> getComments() { return comments; }

    public void setCode(int code) { this.code = code; }
    public void setMessage(String message) { this.message = message; }
    public void setComments(List<CommentDto> comments) { this.comments = comments; }
}
