package com.ihajji.backend.reports.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ihajji.backend.reports.Entity.ReportUserEntity;

public interface ReportUserRepository extends JpaRepository <ReportUserEntity, Long> {
    
}
