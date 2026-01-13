package com.ihajji.backend.profile.dto;

import org.apache.hc.core5.http.HttpStatus;

public class userDto {
    private String username;
    private String profile;
    private int Code;
    private String Message;

    public userDto() {
        this.Code = HttpStatus.SC_OK;
        this.Message = "this ops went ok";

    }

    public userDto(int Code, String Message) {
        this.Code = Code;
        this.Message = Message;

    } public userDto(String username ,  String profile) {
        this.username = username;
        this.profile = profile;
         this.Code = HttpStatus.SC_OK;
        this.Message = "this ops went ok";

    }


    public int getCode() {
        return Code;
    }

    public String getMessage() {
        return Message;
    }

    public String getProfile() {
        return profile;
    }

    public String getUsername() {
        return username;
    }

    public void setCode(int Code) {
        this.Code = Code;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    

}
