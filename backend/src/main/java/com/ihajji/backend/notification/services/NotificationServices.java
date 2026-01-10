package com.ihajji.backend.notification.services;


import java.util.List;
import java.util.Optional;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ihajji.backend.notification.dto.NotificationErrDto;
import com.ihajji.backend.notification.entity.NotificationEntity;
import com.ihajji.backend.notification.repository.NotificationRepository;
import com.ihajji.backend.user.entity.UserEntity;
import com.ihajji.backend.user.repository.UserRepository;

@Service
public class NotificationServices {
    final NotificationRepository repo ;
    final UserRepository userrepo;
    NotificationServices(NotificationRepository repo , UserRepository userrepo){
        this.repo = repo;
        this.userrepo = userrepo;
    }
    public NotificationErrDto load(String username){
        Optional<UserEntity> user = userrepo.findByUsername(username);
        if (!user.isPresent()){
            return new NotificationErrDto(HttpStatus.SC_INTERNAL_SERVER_ERROR, "midleware is nort working");
        }
        Optional<List<NotificationEntity>> notifications = repo.findByReceiver(user.get());
          if (!notifications.isPresent()){
            return new NotificationErrDto(HttpStatus.SC_INTERNAL_SERVER_ERROR, "midleware is nort working");
        }
        return new NotificationErrDto(notifications.get());

    }
}
