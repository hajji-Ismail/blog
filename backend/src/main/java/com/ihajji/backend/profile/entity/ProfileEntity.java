package com.ihajji.backend.profile.entity;

import com.ihajji.backend.user.entity.UserEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity 
@Table(name = "followers")
public class ProfileEntity {
        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower", nullable = false) 
    private UserEntity follower;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following", nullable = false) 
    private UserEntity following;
  
    public UserEntity getFollower() {
        return follower;
    }
  
    public void setFollower(UserEntity follower) {
        this.follower = follower;
    }
    
    public UserEntity getFollowing() {
        return following;
    }

    public void setFollowing(UserEntity following) {
        this.following = following;
    }
    
}
