package com.ihajji.backend.admin.dto;

import java.util.List;

import com.ihajji.backend.reports.Entity.ReportPostEntity;
import com.ihajji.backend.reports.Entity.ReportUserEntity;
import com.ihajji.backend.user.dto.userInterface;

public record AdminDataDto(  
      Long numberOfBandUser,
      Long numberOfUnbandUser,
      List<userInterface> users,
      List<ReportPostEntity> postRerports,
      List<ReportUserEntity> userReports
) {
    
}
