package com.ihajji.backend.user.dto;

import com.ihajji.backend.user.entity.UserEntity;

public class AuthResponse {
    private final UserEntity user;
    private final String accessToken; 
  public AuthResponse(UserEntity user, String accessToken){
    this.accessToken =accessToken;
    this.user = user;
   }
   public String getAccessToken() {
       return accessToken;
   }
   public UserEntity getUser() {
       return user;
   }

}
