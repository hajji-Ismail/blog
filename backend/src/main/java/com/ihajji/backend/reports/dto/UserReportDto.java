package com.ihajji.backend.reports.dto;

import com.ihajji.backend.user.entity.UserEntity;

public interface UserReportDto {
    String getReason();
    UserEntity getReported() ;
}
