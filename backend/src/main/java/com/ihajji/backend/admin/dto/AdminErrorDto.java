package com.ihajji.backend.admin.dto;

import org.apache.hc.core5.http.HttpStatus;

public class AdminErrorDto {
    private int code ;
    private String message;
    private String username;
    private Long post_id;

   public AdminErrorDto(){
        this.code = HttpStatus.SC_OK;
        this.message = "this ops went smouthly";
    }
    public AdminErrorDto(int code ,String message ){
        this.code = code;
        this.message = message;
    }
    public int getCode() {
        return code;
    }
    public Long getPost_id() {
        return post_id;
    }
    public String getUsername() {
        return username;
    }
    public String getMessage() {
        return message;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public void setPost_id(Long post_id) {
        this.post_id = post_id;
    }
    public void setUsername(String username) {
        this.username = username;
    }


}
