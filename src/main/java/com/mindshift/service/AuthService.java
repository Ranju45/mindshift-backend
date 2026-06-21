package com.mindshift.service;

import com.mindshift.dto.request.LoginRequest;
import com.mindshift.dto.request.RegisterRequest;
import com.mindshift.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
