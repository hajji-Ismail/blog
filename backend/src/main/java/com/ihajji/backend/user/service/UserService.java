package com.ihajji.backend.user.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ihajji.backend.user.dto.AuthResponse;
import com.ihajji.backend.user.dto.CreateUserDto;
import com.ihajji.backend.user.dto.ErrorResponse;
import com.ihajji.backend.user.dto.UserCredentiales;
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
            FileUploadService fileUploadService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.fileUploadService = fileUploadService;
    }

    // ========================= REGISTER =========================
public AuthResponse register(CreateUserDto dto) throws ErrorResponse {
    // 1. DEBUG: Verify if Spring successfully bound the data from Postman
    System.out.println("--- Registration Debug ---");
    System.out.println("Email: " + dto.getEmail());
    System.out.println("Username: " + dto.getUsername());
    System.out.println("Has Profile Image: " + (dto.getProfileImage() != null && !dto.getProfileImage().isEmpty()));

    Map<String, String> errors = new HashMap<>();

    // 2. Validation Checks
    if (userRepository.existsByEmail(dto.getEmail())) {
        errors.put("email", "Email already exists");
    }

    if (userRepository.existsByUsername(dto.getUsername())) {
        errors.put("username", "Username already taken");
    }

    // 3. If validation fails, throw the error with the map
    if (!errors.isEmpty()) {
        System.out.println("Validation Errors: " + errors);
        throw new ErrorResponse("Registration failed", errors);
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
                    "user-avatars"
            );
            System.out.println("Upload successful: " + profileImageUrl);
        } catch (IOException e) {
            System.err.println("Cloudinary Upload Error: " + e.getMessage());
            errors.put("profileImage", "Image upload failed: " + e.getMessage());
            throw new ErrorResponse("Registration failed", errors);
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

    String accessToken = jwtUtil.generateToken(
            claims,
            savedUser.getUsername()
    );

    System.out.println("User registered successfully: " + savedUser.getUsername());
    return new AuthResponse(savedUser, accessToken);
}

    // ========================= LOGIN =========================
    public AuthResponse login(UserCredentiales dto) throws ErrorResponse {

        UserEntity user = userRepository
                .findByUsername(dto.getEmail_or_username())
                .or(() -> userRepository.findByEmail(dto.getEmail_or_username()))
                .orElseThrow(() ->
                        new ErrorResponse("Login failed: User not found")
                );

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new ErrorResponse("Login failed: Invalid credentials");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "USER");

        String accessToken = jwtUtil.generateToken(
                claims,
                user.getUsername()
        );

        return new AuthResponse(user, accessToken);
    }
}
