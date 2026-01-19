package com.ihajji.backend.notification.entity;

import com.ihajji.backend.user.entity.UserEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Notification")
public class NotificationEntity {
      @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "sender", nullable = false) // This creates the foreign key column
    private UserEntity sender;
     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "receiver", nullable = false) // This creates the foreign key column
    private UserEntity receiver;
  private  String reason ;
  private NatureEntity nature;

    public Long getId() {
        return id;
    }

    public NatureEntity getNature() {
        return nature;
    }

    public String getReason() {
        return reason;
    }

    public UserEntity getReceiver() {
        return receiver;
    }

    public UserEntity getSender() {
        return sender;
    }

    public void setId(Long id) {
        this.id = id;
    }
  
  public void setNature(NatureEntity nature) {
      this.nature = nature;
  }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setReceiver(UserEntity receiver) {
        this.receiver = receiver;
    }

    public void setSender(UserEntity sender) {
        this.sender = sender;
    }
    
    
  
    

    
}
