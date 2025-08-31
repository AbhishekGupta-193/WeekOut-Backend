package com.weekout.backend.Controllers;

import com.weekout.backend.DTOs.*;
import com.weekout.backend.Model.User;
import com.weekout.backend.Services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@RequestBody SignupRequest req) {
        UserResponse saved = authService.register(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {
        AuthResponse resp = authService.login(req);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<User> getUserById(@PathVariable UUID id) {
        User userResponse = authService.getUserById(id);
        return ResponseEntity.ok(userResponse);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable UUID id,
            @RequestBody UpdateUserRequest req
    ) {
        UserResponse updated = authService.updateUser(id, req);
        return ResponseEntity.ok(updated);
    }

}

