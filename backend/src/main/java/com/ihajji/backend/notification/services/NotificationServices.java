package com.ihajji.backend.notification.services;


import java.util.List;
import java.util.Optional;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ihajji.backend.notification.dto.NotificationDto;
import com.ihajji.backend.notification.dto.NotificationErrDto;
import com.ihajji.backend.notification.entity.NotificationEntity;
import com.ihajji.backend.notification.repository.NotificationRepository;
import com.ihajji.backend.profile.dto.FollowingDto;
import com.ihajji.backend.profile.repository.ProfileRepository;
import com.ihajji.backend.user.entity.UserEntity;
import com.ihajji.backend.user.repository.UserRepository;

@Service
public class NotificationServices {
    final NotificationRepository repo ;
    final UserRepository userrepo;
    final  ProfileRepository profilerepo;
    public NotificationServices(NotificationRepository repo , UserRepository userrepo,ProfileRepository profilerepo){
        this.repo = repo;
        this.userrepo = userrepo;
        this.profilerepo = profilerepo;
    }
public NotificationErrDto load(String username) {
    Optional<UserEntity> user = userrepo.findByUsername(username);
    if (!user.isPresent()) {
        return new NotificationErrDto(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Middleware is not working");
    }
    if (user.get().getIs_baned()){
                return new NotificationErrDto(HttpStatus.SC_UNAUTHORIZED, "the user is banned");

    }

    Optional<List<NotificationEntity>> notifications = repo.findByReceiver(user.get());
    if (!notifications.isPresent() || notifications.get().isEmpty()) {
        return new NotificationErrDto(HttpStatus.SC_OK, "No notifications found");
    }

    // Map entities to DTOs
    List<NotificationDto> dtoList = notifications.get().stream()
        .map(n -> new NotificationDto(
            n.getId(),
            n.getMessage(),
            n.getNature().toString(),
            n.getSender().getUsername(),
            n.getReceiver().getUsername(),
            Boolean.TRUE.equals(n.getRead()),
            n.getReceiver().getProfileImageUrl()
        ))
        .toList();

    return new NotificationErrDto(dtoList);
}

    public void SavePosts(UserEntity user){
     List<FollowingDto> followings = this.profilerepo.findAllByFollowing(user);
     for (FollowingDto  following : followings) {
        NotificationEntity data = new NotificationEntity();
        data.setReceiver(following.getFollower());
        data.setSender(user);
        data.setMessage(String.format("new Post From %S", user.getUsername()));
        data.setNature("post");
        this.repo.save(data);
        
     }
        

    }

    public NotificationErrDto markAsRead(String username, Long notificationId) {
        Optional<UserEntity> user = userrepo.findByUsername(username);
        if (!user.isPresent()) {
            return new NotificationErrDto(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Middleware is not working");
        }
          if (user.get().getIs_baned()){
                return new NotificationErrDto(HttpStatus.SC_UNAUTHORIZED, "the user is banned");

    }
        if (notificationId == null) {
            return new NotificationErrDto(HttpStatus.SC_BAD_REQUEST, "Notification id is required");
        }

        Optional<NotificationEntity> notification = repo.findByIdAndReceiver(notificationId, user.get());
        if (!notification.isPresent()) {
            return new NotificationErrDto(HttpStatus.SC_BAD_REQUEST, "Notification not found");
        }

        NotificationEntity entity = notification.get();
        this.repo.delete(entity);
        return new NotificationErrDto();
    }
        public void SaveUserReports(UserEntity user){
     List<UserEntity> admins = this.userrepo.findByRole("ADMIN");
     for (UserEntity   admin : admins) {
        NotificationEntity data = new NotificationEntity();
        data.setReceiver(admin);
        data.setSender(user);
        data.setMessage(String.format("new User has been reported"));
        data.setNature("report");
        this.repo.save(data);
     }
        

    }
    public void SavePostReports(UserEntity user){
     List<UserEntity> admins = this.userrepo.findByRole("ADMIN");
     for (UserEntity   admin : admins) {
        NotificationEntity data = new NotificationEntity();
        data.setReceiver(admin);
        data.setSender(user);
        data.setMessage(String.format("new Post has been reported"));
        data.setNature("report");
        this.repo.save(data);
     }
        

    }
       
    
}
