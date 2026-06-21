package com.mindshift.controller;

import com.mindshift.dto.response.ProgressResponse;
import com.mindshift.model.User;
import com.mindshift.service.ProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/progress")
@RequiredArgsConstructor
public class ProgressController {

    private final ProgressService progressService;

    @GetMapping
    public ResponseEntity<ProgressResponse> get(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(progressService.get(user));
    }

    @PostMapping("/complete/{day}")
    public ResponseEntity<ProgressResponse> completeDay(@AuthenticationPrincipal User user,
                                                          @PathVariable int day) {
        return ResponseEntity.ok(progressService.completeDay(user, day));
    }
}
