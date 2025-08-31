package com.weekout.backend.DTOs;
import lombok.Data;

import java.util.Set;
@Data
public class UpdateUserRequest {
    private String name;
    private String email;
    private String phoneNumber;
    private String profilePictureUrl;
    private String bio;
    private Set<String> interests;
}
