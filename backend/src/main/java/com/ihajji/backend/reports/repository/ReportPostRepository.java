package com.ihajji.backend.reports.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ihajji.backend.reports.Entity.ReportPostEntity;

@Repository
public interface ReportPostRepository extends JpaRepository<ReportPostEntity, Long> {
    
}
