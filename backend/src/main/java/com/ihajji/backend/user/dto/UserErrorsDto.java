package com.ihajji.backend.user.dto;

public class UserErrorsDto {
    private int code;
    private String message;
    private Boolean hasErr;
    private String username;
    private String password;
    private String email;

    public UserErrorsDto(int code, String message, Boolean hasErr, String username, String password, String email) {
        this.code = code;
        this.message = message;
        this.hasErr = hasErr;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public UserErrorsDto() {
        this.code = 200;
        this.message = "this app went smouthly";
        this.hasErr = false;
    }

    // 3. Getters
    public int getCode() { return code; }
    public String getMessage() { return message; }
    public Boolean getHasErr() { return hasErr; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }

    // 4. Setters
    public void setCode(int code) { this.code = code; }
    public void setMessage(String message) { this.message = message; }
    public void setHasErr(Boolean hasErr) { this.hasErr = hasErr; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setEmail(String email) { this.email = email; }
}