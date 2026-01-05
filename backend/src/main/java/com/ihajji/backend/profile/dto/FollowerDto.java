package com.ihajji.backend.profile.dto;

import org.apache.hc.core5.http.HttpStatus;

public class FollowerDto {
    private String Followed;
    private int Code;
    private String Message;
  public  FollowerDto(){
        this.Code = HttpStatus.SC_OK;
        this.Message= "this ops went ok";

    }
      public FollowerDto(int Code, String Message){
        this.Code = Code;
        this.Message= Message;

    }
    public String getFollowed() {
        return Followed;
    }
    public void setFollowed(String followed) {
        Followed = followed;
    }
    public int getCode() {
        return Code;
    }
    public void setCode(int code) {
        Code = code;
    }
    public String getMessage() {
        return Message;
    }
    public void setMessage(String message) {
        Message = message;
    }
    
}
