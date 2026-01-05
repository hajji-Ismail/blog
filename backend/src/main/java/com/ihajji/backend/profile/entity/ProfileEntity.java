package com.ihajji.backend.profile.entity;

import com.ihajji.backend.user.entity.UserEntity;

import jakarta.persistence.*;

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
    @JoinColumn(name = "followed", nullable = false) 
    private UserEntity followed;
    public UserEntity getFollowed() {
        return followed;
    }
    public UserEntity getFollower() {
        return follower;
    }
    public void setFollowed(UserEntity followed) {
        this.followed = followed;
    }
    public void setFollower(UserEntity follower) {
        this.follower = follower;
    }
}
