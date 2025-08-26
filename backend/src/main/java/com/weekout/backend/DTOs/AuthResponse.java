package com.weekout.backend.DTOs;

import com.weekout.backend.Model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String tokenType; // "Bearer"
    private long expiresAtEpochMs;
    private User user;
}

