package com.weekout.backend.Services;

import com.weekout.backend.DTOs.*;
import com.weekout.backend.Model.User;
import com.weekout.backend.Repositories.UserRepository;
import com.weekout.backend.Security.JwtService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public UserResponse register(SignupRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalStateException("Email already in use");
        }

        User user = User.builder()
                .name(req.getName())
                .email(req.getEmail())
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .phoneNumber(req.getPhoneNumber())
                .profilePictureUrl(req.getProfilePictureUrl())
                .bio(req.getBio())
                .interests(req.getInterests() == null ? new java.util.HashSet<>() : req.getInterests())
                .build();

        try {
            User saved = userRepository.save(user);
            return toUserResponse(saved);
        } catch (DataIntegrityViolationException ex) {
            // DB uniqueness race: email already taken
            throw new IllegalStateException("Email already in use");
        }
    }

    public AuthResponse login(LoginRequest req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email/password"));

        boolean matches = passwordEncoder.matches(req.getPassword(), user.getPasswordHash());
        if (!matches) throw new IllegalArgumentException("Invalid email/password");

        String token = jwtService.generateToken(user.getEmail());
        long expiresAt = System.currentTimeMillis() + 3600_000L; // match jwt.expiration-ms if you want

        return new AuthResponse(token, "Bearer", expiresAt, user);
    }

    private UserResponse toUserResponse(User u) {
        UserResponse r = new UserResponse();
        r.setId(u.getId());
        r.setName(u.getName());
        r.setEmail(u.getEmail());
        r.setPhoneNumber(u.getPhoneNumber());
        r.setProfilePictureUrl(u.getProfilePictureUrl());
        r.setBio(u.getBio());
        r.setInterests(u.getInterests());
        r.setCreatedAt(u.getCreatedAt());
        return r;
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserResponse updateUser(UUID userId, UpdateUserRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update only if values are provided
        if (req.getName() != null) user.setName(req.getName());
        if (req.getEmail() != null) user.setEmail(req.getEmail());
        if (req.getPhoneNumber() != null) user.setPhoneNumber(req.getPhoneNumber());
        if (req.getProfilePictureUrl() != null) user.setProfilePictureUrl(req.getProfilePictureUrl());
        if (req.getBio() != null) user.setBio(req.getBio());
        if (req.getInterests() != null) user.setInterests(req.getInterests());

        User updated = userRepository.save(user);
        return toUserResponse(updated);
    }

}

