package com.ihajji.backend.profile.entity;

import com.ihajji.backend.user.entity.UserEntity;

import jakarta.persistence.*;

@Entity 
@Table(name = "followers")
public class profileEntity {
        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower", nullable = false) 
    private UserEntity follower;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followed", nullable = false) 
    private UserEntity followed;
}
