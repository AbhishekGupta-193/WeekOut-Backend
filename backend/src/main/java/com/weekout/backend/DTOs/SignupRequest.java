package com.weekout.backend.DTOs;

import lombok.Data;
import java.util.Set;

@Data
public class SignupRequest {
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private String profilePictureUrl;
    private String bio;
    private Set<String> interests;
}
