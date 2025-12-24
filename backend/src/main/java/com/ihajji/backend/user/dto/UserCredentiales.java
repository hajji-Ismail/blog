package com.ihajji.backend.user.dto;

import jakarta.validation.constraints.NotBlank;

public class UserCredentiales {
@NotBlank
private String email_or_username ;
@NotBlank 
private String password;
public String getEmail_or_username() {
    return email_or_username;
}
public String getPassword() {
    return password;
}
public void setEmail_or_username(String email_or_username) {
    this.email_or_username = email_or_username;
}

    
}