package com.ihajji.backend.profile.dto;

import java.util.List;

import com.ihajji.backend.posts.dto.PostFeedResponse;

public record ProfileDto(
     String username,
     String profileImage,
        Long followers,
        Long following,
         Boolean  profile,

        List<PostFeedResponse> post) {}
