package com.mindshift.controller;

import com.mindshift.dto.request.JournalRequest;
import com.mindshift.dto.response.JournalResponse;
import com.mindshift.dto.response.TriggerWordResponse;
import com.mindshift.model.User;
import com.mindshift.service.JournalService;
import com.mindshift.service.impl.JournalAnalyticsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/journal")
@RequiredArgsConstructor
public class JournalController {

    private final JournalService journalService;
    private final JournalAnalyticsService analyticsService;

    @PostMapping
    public ResponseEntity<JournalResponse> create(@AuthenticationPrincipal User user,
                                                    @Valid @RequestBody JournalRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(journalService.create(user, request));
    }

    @GetMapping
    public ResponseEntity<List<JournalResponse>> getAll(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(journalService.getAll(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal User user, @PathVariable Long id) {
        journalService.delete(user, id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Top-K most frequent words across all of a user's journal entries.
     * See JournalAnalyticsService for the heap-based algorithm.
     */
    @GetMapping("/insights/top-triggers")
    public ResponseEntity<List<TriggerWordResponse>> topTriggers(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "5") int k) {
        List<TriggerWordResponse> result = analyticsService.getTopTriggers(user, k).stream()
                .map(t -> TriggerWordResponse.builder().word(t.word()).frequency(t.frequency()).build())
                .toList();
        return ResponseEntity.ok(result);
    }
}
