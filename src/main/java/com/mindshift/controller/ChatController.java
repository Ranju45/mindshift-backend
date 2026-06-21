package com.mindshift.controller;

import com.mindshift.dto.request.ChatRequest;
import com.mindshift.dto.response.ChatHistoryItem;
import com.mindshift.dto.response.ChatResponse;
import com.mindshift.model.User;
import com.mindshift.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<ChatResponse> send(@AuthenticationPrincipal User user,
                                               @Valid @RequestBody ChatRequest request) {
        return ResponseEntity.ok(chatService.sendMessage(user, request));
    }

    @GetMapping("/history")
    public ResponseEntity<List<ChatHistoryItem>> history(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(chatService.getHistory(user));
    }
}
