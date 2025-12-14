package com.ihajji.backend.user.dto;

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
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public String getUsername() {
        return username;
    }
    
    

}
