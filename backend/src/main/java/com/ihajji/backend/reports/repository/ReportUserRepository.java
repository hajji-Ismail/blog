package com.ihajji.backend.reports.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ihajji.backend.reports.Entity.ReportUserEntity;
import com.ihajji.backend.reports.dto.UserReportDto;

public interface ReportUserRepository extends JpaRepository <ReportUserEntity, Long> {
     List<UserReportDto> findAllBy();  
}
