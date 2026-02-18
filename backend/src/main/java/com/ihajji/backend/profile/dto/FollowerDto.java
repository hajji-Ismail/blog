package com.ihajji.backend.profile.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public class FollowerDto {
    @JsonAlias({ "Followed", "followed" })
    private String followed;
  
 
    public String getFollowed() {
        return followed;
    }
    public void setFollowed(String followed) {
        this.followed = followed;
    }
  
    
}
