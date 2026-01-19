package com.ihajji.backend.profile.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ihajji.backend.profile.dto.FollowingDto;
import com.ihajji.backend.profile.entity.ProfileEntity;
import com.ihajji.backend.user.entity.UserEntity;
@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity , Long> {
Boolean existsByFollowerAndFollowing(UserEntity follower, UserEntity followed);
    
    // Delete a specific follow relationship
    void deleteByFollowerAndFollowing(UserEntity follower, UserEntity followed);
     // Count how many users a specific user is following
    Long countByFollower(UserEntity follower);

    // Count how many followers a specific user has
    Long countByFollowing(UserEntity followed);
 List<FollowingDto> findAllByFollowing(UserEntity following);    
} 