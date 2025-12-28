package com.ihajji.backend.user.dto;

import org.springframework.web.multipart.MultipartFile;



public class CreateUserDto {
    
    private String username;
  
    private String email ;
   
    private String password ;
    private MultipartFile profileImage; 

  

    
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public String getUsername() {
        return username;
    }
    public MultipartFile getProfileImage() { return profileImage; }
    public void setProfileImage(MultipartFile profileImage) { this.profileImage = profileImage; }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    
    

}
