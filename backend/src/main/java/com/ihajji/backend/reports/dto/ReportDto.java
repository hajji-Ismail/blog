package com.ihajji.backend.reports.dto;

import org.apache.hc.core5.http.HttpStatus;

public class ReportDto {
    private int code;
    private String message;
    private String Username;
    private String reason;
    private Long Post_id;
  public  ReportDto(){
        this.code = HttpStatus.SC_OK;
        this.message = "this ops went smouthly";
    }
     public ReportDto( int code , String message){
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
    public Long getPost_id() {
        return Post_id;
    }
    public String getReason() {
        return reason;
    }
    public String getUsername() {
        return Username;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public void setPost_id(Long post_id) {
        Post_id = post_id;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }
    public void setUsername(String username) {
        Username = username;
    }

    
}
