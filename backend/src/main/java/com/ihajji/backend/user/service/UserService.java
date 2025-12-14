package com.ihajji.backend.user.service;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ihajji.backend.user.dto.AuthResponse;
import com.ihajji.backend.user.dto.CreateUserDto;
import com.ihajji.backend.user.dto.ErrorResponse;
import com.ihajji.backend.user.dto.UserCredentiales;
import com.ihajji.backend.user.entity.UserEntity;
import com.ihajji.backend.user.repository.UserRepository;
import com.ihajji.backend.user.utils.JwtUtil;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public UserEntity register(CreateUserDto dto) throws ErrorResponse {

        Map<String, String> errors = new HashMap<>();

        if (userRepository.existsByEmail(dto.getEmail())) {
            errors.put("email", "Email already exists");
        }

        if (userRepository.existsByUsername(dto.getUsername())) {
            errors.put("username", "Username already taken");
        }

        if (!errors.isEmpty()) {
            // Throw your DTO as exception
            throw new ErrorResponse("Registration failed", errors);
        }

        UserEntity user = new UserEntity();
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        return userRepository.save(user);
    }
    public AuthResponse login(UserCredentiales dto) throws ErrorResponse{
       
    UserEntity userInfo = userRepository.findByUsername(dto.getEmail_or_username())
    .or(() -> userRepository.findByEmail(dto.getEmail_or_username()))
    .orElseThrow(() -> new ErrorResponse("Login failed: User not found"));
    if(!passwordEncoder.matches(dto.getPassword(), userInfo.getPassword())){
        throw new ErrorResponse("Login failed: Invalid credentials ");
    }
      Map<String, Object> claims = new HashMap<>();
    claims.put("role", "user");
    String accessToken = this.jwtUtil.generateToken(claims, userInfo.getUsername()); // Renamed token to accessToken
    
    // Instead of creating the cookie here, just return the token and user info
    return new AuthResponse(userInfo, accessToken);

        

    }
}