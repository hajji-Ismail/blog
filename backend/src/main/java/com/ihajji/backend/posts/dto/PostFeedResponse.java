package com.ihajji.backend.posts.dto;

import java.time.LocalDateTime;
import java.util.List;

public record PostFeedResponse(
    Long id,
    String title,
    String content,
    String username,
    String profileImageUrl,
    int reactionCount,
    int commentCount,
    LocalDateTime createdAt,
    List<String> medias ,
    boolean reacted
) {}