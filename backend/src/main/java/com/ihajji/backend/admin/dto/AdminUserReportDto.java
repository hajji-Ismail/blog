package com.ihajji.backend.admin.dto;

import java.time.LocalDateTime;

public record AdminUserReportDto(
        Long id,
        String reason,
        LocalDateTime time,
        String reportedUsername,
        String reporterUsername) {
}
