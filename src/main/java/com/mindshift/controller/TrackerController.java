package com.mindshift.controller;

import com.mindshift.dto.request.TrackerRequest;
import com.mindshift.dto.response.TrackerResponse;
import com.mindshift.model.User;
import com.mindshift.service.TrackerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/tracker")
@RequiredArgsConstructor
public class TrackerController {

    private final TrackerService trackerService;

    @PostMapping
    public ResponseEntity<TrackerResponse> save(@AuthenticationPrincipal User user,
                                                  @Valid @RequestBody TrackerRequest request) {
        return ResponseEntity.ok(trackerService.save(user, request));
    }

    @GetMapping
    public ResponseEntity<Map<String, TrackerResponse>> getAll(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(trackerService.getAll(user));
    }
}
