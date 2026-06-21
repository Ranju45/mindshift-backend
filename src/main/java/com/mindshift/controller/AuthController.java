package com.mindshift.controller;

import com.mindshift.dto.request.LoginRequest;
import com.mindshift.dto.request.RegisterRequest;
import com.mindshift.dto.response.AuthResponse;
import com.mindshift.dto.response.UserResponse;
import com.mindshift.model.User;
import com.mindshift.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
public ResponseEntity<UserResponse> me(@AuthenticationPrincipal User user) {
    if (user == null) {
        return ResponseEntity.status(401).build();
    }
    return ResponseEntity.ok(UserResponse.from(user));
}
}
