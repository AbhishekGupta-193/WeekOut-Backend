package com.weekout.backend.DTOs;

import lombok.Data;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Data
public class UserResponse {
    private UUID id;
    private String name;
    private String email;
    private String phoneNumber;
    private String profilePictureUrl;
    private String bio;
    private Set<String> interests;
    private Instant createdAt;
}

