package com.ihajji.backend.user.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ihajji.backend.user.dto.AuthResponse;
import com.ihajji.backend.user.dto.CreateUserDto;
import com.ihajji.backend.user.dto.UserCredentiales;
import com.ihajji.backend.user.dto.UserErrorsDto;
import com.ihajji.backend.user.entity.UserEntity;
import com.ihajji.backend.user.repository.UserRepository;
import com.ihajji.backend.user.utils.FileUploadService;
import com.ihajji.backend.user.utils.JwtUtil;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final FileUploadService fileUploadService;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil,
            FileUploadService fileUploadService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.fileUploadService = fileUploadService;
    }

    // ========================= REGISTER =========================
    public AuthResponse register(CreateUserDto dto) {

        UserErrorsDto UserERR = new UserErrorsDto(200, "", false, "", "", "");
        if (dto.getPassword().length() < 8){
            UserERR.setPassword("the password should be at least 8 characters wide");
             UserERR.setCode(HttpStatus.SC_BAD_REQUEST);
            UserERR.setHasErr(true);
        }
        if (dto.getUsername().length() < 3){
            UserERR.setUsername("the username should be at least 3 characters wide");
             UserERR.setCode(HttpStatus.SC_BAD_REQUEST);
            UserERR.setHasErr(true);
        }

     
        if (userRepository.existsByEmail(dto.getEmail())) {
            UserERR.setEmail("Email Already taken");
            UserERR.setCode(HttpStatus.SC_BAD_REQUEST);
            UserERR.setHasErr(true);
        }

        if (userRepository.existsByUsername(dto.getUsername())) {
            UserERR.setUsername("username Already taken");
            UserERR.setCode(HttpStatus.SC_BAD_REQUEST);
            UserERR.setHasErr(true);
        }

        // 3. If validation fails, throw the error with the map
        if (UserERR.getHasErr()) {
            return new AuthResponse(UserERR);

        }

        // 4. Create User Entity
        UserEntity user = new UserEntity();
        String profileImageUrl = null;

        // 5. Handle Cloudinary Image Upload
        if (dto.getProfileImage() != null && !dto.getProfileImage().isEmpty()) {
            try {
                System.out.println("Attempting Cloudinary upload...");
                profileImageUrl = fileUploadService.uploadFile(
                        dto.getProfileImage(),
                        "user-avatars");
                System.out.println("Upload successful: " + profileImageUrl);
            } catch (IOException e) {
                System.err.println("Cloudinary Upload Error: " + e.getMessage());
                UserERR.setMessage(e.getMessage());
                UserERR.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return new AuthResponse(UserERR);
            }
        }

        // 6. Map DTO to Entity and Save
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setProfileImageUrl(profileImageUrl);

        UserEntity savedUser = userRepository.save(user);

        // 7. Generate JWT Token
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "USER");
        claims.put("is_banned",false);

        String accessToken = jwtUtil.generateToken(
                claims,
                savedUser.getUsername());
        UserERR.setCode(HttpStatus.SC_OK);
        UserERR.setMessage("User registered successfully");
        return new AuthResponse(UserERR, accessToken);
    }

    // ========================= LOGIN =========================
    public AuthResponse login(UserCredentiales dto) {
        UserErrorsDto UserErr = new UserErrorsDto();

        Optional<UserEntity> userOpt = userRepository
                .findByUsername(dto.getEmail_or_username())
                .or(() -> userRepository.findByEmail(dto.getEmail_or_username()));

        if (userOpt.isEmpty()) {
            UserErr.setMessage("invalid credential");
            UserErr.setCode(HttpStatus.SC_BAD_REQUEST); // Bad Request
            UserErr.setHasErr(false);
            return new AuthResponse(UserErr);
        }
        UserEntity user = userOpt.get();
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {

            UserErr.setMessage("invalid credentiel");
            UserErr.setCode(HttpStatus.SC_BAD_REQUEST);
            return new AuthResponse(UserErr);
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "USER");
        claims.put("is_banned",user.getIs_baned());

        String accessToken = jwtUtil.generateToken(
                claims,
                user.getUsername());

        return new AuthResponse(UserErr, accessToken);
    }
}
