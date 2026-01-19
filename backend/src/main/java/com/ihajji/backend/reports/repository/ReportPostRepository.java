package com.ihajji.backend.reports.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ihajji.backend.reports.Entity.ReportPostEntity;
import com.ihajji.backend.reports.dto.PostreportDto;

@Repository
public interface ReportPostRepository extends JpaRepository<ReportPostEntity, Long> {
     List<PostreportDto> findAllBy();  
}
