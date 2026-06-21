package com.mindshift.service.impl;

import com.mindshift.dto.request.LoginRequest;
import com.mindshift.dto.request.RegisterRequest;
import com.mindshift.dto.response.AuthResponse;
import com.mindshift.dto.response.UserResponse;
import com.mindshift.exception.DuplicateResourceException;
import com.mindshift.model.DailyProgress;
import com.mindshift.model.User;
import com.mindshift.repository.DailyProgressRepository;
import com.mindshift.repository.UserRepository;
import com.mindshift.security.JwtUtil;
import com.mindshift.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final DailyProgressRepository progressRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("An account with this email already exists");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .severity(request.getSeverity())
                .goal(request.getGoal())
                .impacts(request.getImpacts())
                .build();
        userRepository.save(user);

        DailyProgress progress = DailyProgress.builder()
                .user(user)
                .currentDay(1)
                .completedDays(java.util.List.of())
                .godModeScore(0)
                .build();
        progressRepository.save(progress);

        String token = jwtUtil.generateToken(user);
        return AuthResponse.builder().token(token).user(UserResponse.from(user)).build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalStateException("User vanished after authentication"));

        String token = jwtUtil.generateToken(user);
        return AuthResponse.builder().token(token).user(UserResponse.from(user)).build();
    }
}
