package com.ihajji.backend.admin.dto;

import java.util.List;

import com.ihajji.backend.reports.dto.PostreportDto;
import com.ihajji.backend.reports.dto.UserReportDto;
import com.ihajji.backend.user.dto.userInterface;

public record AdminDataDto(  
      Long numberOfBandUser,
      Long numberOfUnbandUser,
      List<userInterface> users,
      List<PostreportDto> postRerports,
      List<UserReportDto> userReports
) {
    
}
