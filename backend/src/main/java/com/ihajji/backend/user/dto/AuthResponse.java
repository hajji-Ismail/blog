package com.ihajji.backend.user.dto;


public class AuthResponse {
    private  UserErrorsDto user;
    private  String accessToken; 
  public AuthResponse(UserErrorsDto user, String accessToken){
    this.accessToken =accessToken;
    this.user = user;
   }
   public AuthResponse(){

   }
   public AuthResponse(UserErrorsDto user){
    this.user = user;

   }
   public String getAccessToken() {
       return accessToken;
   }
   public UserErrorsDto getUser() {
       return user;
   }
   public void setAccessToken(String accessToken) {
       this.accessToken = accessToken;
   }
   public void setUser(UserErrorsDto user) {
       this.user = user;
   }
   

}
