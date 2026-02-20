package com.ihajji.backend.reports.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ihajji.backend.reports.Entity.ReportUserEntity;
import com.ihajji.backend.user.entity.UserEntity;

public interface ReportUserRepository extends JpaRepository <ReportUserEntity, Long> {
     List<ReportUserEntity> findAll();  
       void deleteByReported(UserEntity reported);

}
