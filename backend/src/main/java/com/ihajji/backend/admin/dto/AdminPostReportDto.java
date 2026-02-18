package com.ihajji.backend.admin.dto;

import java.time.LocalDateTime;

public record AdminPostReportDto(
        Long id,
        String reason,
        LocalDateTime time,
        Long postId,
        String postTitle,
        String postOwnerUsername,
        String reporterUsername) {
}
