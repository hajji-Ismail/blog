package com.ihajji.backend.profile.service;

import java.util.Optional;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ihajji.backend.posts.service.PostService;
import com.ihajji.backend.profile.dto.FollowerDto;
import com.ihajji.backend.profile.dto.FollowerErrorDto;
import com.ihajji.backend.profile.dto.ProfileDto;
import com.ihajji.backend.profile.dto.userDto;
import com.ihajji.backend.profile.entity.ProfileEntity;
import com.ihajji.backend.profile.repository.ProfileRepository;
import com.ihajji.backend.user.entity.UserEntity;
import com.ihajji.backend.user.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class ProfileService {
    final ProfileRepository repo;
    final UserRepository userRepo;
    final PostService postService;

    ProfileService(ProfileRepository repo, UserRepository userRepo, PostService postService) {
        this.repo = repo;
        this.userRepo = userRepo;
        this.postService = postService;
    }

 @Transactional
    public FollowerErrorDto Follow(String username, FollowerDto Dto) {
        Optional<UserEntity> follower = userRepo.findByUsername(username);
        if (!follower.isPresent()) {
            return new FollowerErrorDto(HttpStatus.SC_INTERNAL_SERVER_ERROR, "midleware is not working properlly");
        }
        if(follower.get().getIs_baned()){
                        return new FollowerErrorDto(HttpStatus.SC_UNAUTHORIZED, "you are banned");

        }

        Optional<UserEntity> followed = userRepo.findByUsername(Dto.getFollowed());
        if (!followed.isPresent()) {
            return new FollowerErrorDto(HttpStatus.SC_BAD_REQUEST, "you are following sommeone that it does not exist");
        }
        if (this.repo.existsByFollowerAndFollowing(follower.get(), followed.get())) {
            repo.deleteByFollowerAndFollowing(follower.get(), followed.get());
            return new FollowerErrorDto();
        }

        ProfileEntity profile = new ProfileEntity();
        profile.setFollower(follower.get());
        profile.setFollowing(followed.get());
        repo.save(profile);
        return new FollowerErrorDto();

    }

    public userDto loadUser(String username) {
        Optional<UserEntity> user = this.userRepo.findByUsername(username);
        if (!user.isPresent()) {
            return new userDto(HttpStatus.SC_INTERNAL_SERVER_ERROR, "the midleware isn't working");
        }
        if (user.get().getIs_baned()){
            return new userDto(HttpStatus.SC_UNAUTHORIZED, "you are baned");
        }
        return new userDto(user.get().getUsername(), user.get().getProfileImageUrl());

    }

    public ProfileDto loadProfile(String username, String profile) {
        Optional<UserEntity> user = this.userRepo.findByUsername(profile);
        if (!user.isPresent()) {
            return null;

        }
        if (user.get().getIs_baned()){
            return null;
        }

        

        Optional<UserEntity> profileUser = this.userRepo.findByUsername(profile);
        boolean isFollowing = profileUser.isPresent() && this.repo.existsByFollowerAndFollowing(profileUser.get(), user.get());

        return new ProfileDto(
                username,
                user.get().getProfileImageUrl(),
                this.repo.countByFollower(user.get()),
                this.repo.countByFollowing(user.get()),
                profile.equals(username),
                isFollowing,
                this.postService.getPostbyUsername(username));
    }


}
