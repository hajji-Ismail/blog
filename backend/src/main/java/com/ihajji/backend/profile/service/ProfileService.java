package com.ihajji.backend.profile.service;

import java.util.Optional;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ihajji.backend.profile.dto.FollowerDto;
import com.ihajji.backend.profile.entity.ProfileEntity;
import com.ihajji.backend.profile.repository.ProfileRepository;
import com.ihajji.backend.user.repository.UserRepository;
import com.ihajji.backend.user.entity.UserEntity;

@Service
public class ProfileService {
    final ProfileRepository repo;
    final UserRepository userRepo;

    ProfileService(ProfileRepository repo, UserRepository userRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
    }

    public FollowerDto Follow(Long userId, FollowerDto Dto) {
        Optional<UserEntity> follower = userRepo.findById(userId);
        if (!follower.isPresent()) {
            return new FollowerDto(HttpStatus.SC_INTERNAL_SERVER_ERROR, "midleware is not working properlly");
        }

        Optional<UserEntity> followed = userRepo.findByUsername(Dto.getFollowed());
        if (!followed.isPresent()) {
            return new FollowerDto(HttpStatus.SC_BAD_REQUEST, "you are following sommeone that it does not exist");
        }
        if (this.repo.exexistsByFollowerAndFollowed(follower.get(), followed.get())) {
            repo.deleteByFollowerAndFollowed(follower.get(), followed.get());
            return new FollowerDto();
        }

        ProfileEntity profile = new ProfileEntity();
        profile.setFollower(follower.get());
        profile.setFollowed(followed.get());
        repo.save(profile);
        return new FollowerDto();

    }
}
