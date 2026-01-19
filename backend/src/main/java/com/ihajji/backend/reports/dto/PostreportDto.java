package com.ihajji.backend.reports.dto;

import java.time.LocalDateTime;

import com.ihajji.backend.posts.entity.PostEntity;

public interface PostreportDto {
    String getReason();
    LocalDateTime getTime();
    PostEntity getPost();
}
