package com.ihajji.backend.reports.Entity;

import java.time.LocalDateTime;

import com.ihajji.backend.user.entity.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_reports")
public class ReportUserEntity {
  @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter", nullable = false) 
    private UserEntity reporter;
      @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported", nullable = false) 
    private UserEntity reported;
  @Column(name = "reason", nullable = false)
    private String reason;
        @Column(name = "time_stamp") 
    private LocalDateTime time = LocalDateTime.now();
public LocalDateTime getTime() {
    return time;
}
public void setTime(LocalDateTime time) {
    this.time = time;
}

    public void setId(Long id) {
        this.id = id;
    }
   
    public void setReason(String reason) {
        this.reason = reason;
    }
    public void setReporter(UserEntity reporter) {
        this.reporter = reporter;
    }
    public Long getId() {
        return id;
    }
 
    public String getReason() {
        return reason;
    }
    public UserEntity getReporter() {
        return reporter;
    }
    public UserEntity getReported() {
        return reported;
    }
    public void setReported(UserEntity reported) {
        this.reported = reported;
    }
    
}
