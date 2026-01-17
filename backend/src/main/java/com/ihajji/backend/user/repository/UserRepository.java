package com.ihajji.backend.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ihajji.backend.user.entity.UserEntity;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>  {
    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);
       boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    List<UserEntity> findByUsernameContainingIgnoreCase(String usernamePart);

}