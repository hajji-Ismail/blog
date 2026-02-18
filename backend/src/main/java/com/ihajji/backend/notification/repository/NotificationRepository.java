package com.ihajji.backend.notification.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ihajji.backend.notification.entity.NotificationEntity;
import com.ihajji.backend.user.entity.UserEntity;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long>{
Optional<List<NotificationEntity>> findByReceiver(UserEntity user);
Optional<NotificationEntity> findByIdAndReceiver(Long id, UserEntity receiver);
}
