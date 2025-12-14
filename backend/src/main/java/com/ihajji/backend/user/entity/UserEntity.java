package com.ihajji.backend.user.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    @Column(name = "username", nullable= false , unique = true)
    private String username ;
    @Column(name = "email", unique = true, nullable = false)
    private String email ;
    @Column(name = "password", unique = false, nullable = false)
    private String password;
    @Column(name = "role", nullable = true, unique = false)
    private String role = "user";
    @Column(name = "banned", nullable = true, unique = false)
    private Boolean is_baned = false;
    public String getEmail() {
        return email;
    }
    public Long getId() {
        return id;
    }
    public Boolean getIs_baned() {
        return is_baned;
    }
    public String getRole() {
        return role;
    }
    public String getPassword() {
        return password;
    }
    public String getUsername() {
        return username;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setIs_baned(Boolean is_baned) {
        this.is_baned = is_baned;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setRole(String role) {
        this.role = role;
    }
    @Override
  
    public String toString() {
        
        return "regestration went well";
    }


    

    
}