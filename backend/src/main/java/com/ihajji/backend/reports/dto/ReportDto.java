package com.ihajji.backend.reports.dto;

public class ReportDto {
    private String Username;
    private String reason;
    private Long Post_id;

    public Long getPost_id() {
        return Post_id;
    }
    public String getReason() {
        return reason;
    }
    public String getUsername() {
        return Username;
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
