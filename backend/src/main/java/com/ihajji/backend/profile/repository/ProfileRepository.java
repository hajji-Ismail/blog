package com.ihajji.backend.profile.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ihajji.backend.profile.entity.ProfileEntity;
import com.ihajji.backend.user.entity.UserEntity;
@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity , Long> {
Boolean exexistsByFollowerAndFollowed(UserEntity follower, UserEntity followed);
    
    // Delete a specific follow relationship
    void deleteByFollowerAndFollowed(UserEntity follower, UserEntity followed);
    
} 