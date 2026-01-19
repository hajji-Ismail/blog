package com.ihajji.backend.profile.dto;

import com.ihajji.backend.user.entity.UserEntity;
public interface FollowingDto {
    UserEntity getFollower();
}