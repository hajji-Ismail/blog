package com.ihajji.backend.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ihajji.backend.user.entity.UserEntity;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>  {
    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);
       boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    
}