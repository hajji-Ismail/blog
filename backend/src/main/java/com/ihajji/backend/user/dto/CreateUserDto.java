package com.ihajji.backend.user.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateUserDto {
    @NotBlank(message = "it should be at least 3 characters long")
    @Size(min = 3 ,message = "it should be at least 3 characters long")
    private String username;
    @NotBlank(message = "it can't be empty")
    @Email(message = "invalid email")
    private String email ;
    @NotBlank(message = "it can't be empty")
    @Size(min = 8 , message = "the password should be at least 8 Charcters")
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
