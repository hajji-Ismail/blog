package com.ihajji.backend.profile.dto;

import org.apache.hc.core5.http.HttpStatus;

public class FollowerErrorDto {
        private int Code;
    private String Message;

    public FollowerErrorDto() {
        this.Code = HttpStatus.SC_OK;
        this.Message = "this ops went ok";

    }

    public FollowerErrorDto(int Code, String Message) {
        this.Code = Code;
        this.Message = Message;

    }
    public int getCode() {
        return Code;
    }
    public String getMessage() {
        return Message;
    }
    public void setCode(int code) {
        Code = code;
    }
    public void setMessage(String message) {
        Message = message;
    }
    
}
