package com.mindshift.dto.response;

import com.mindshift.model.ChatMessage;
import com.mindshift.model.enums.MessageRole;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ChatHistoryItem {
    private MessageRole role;
    private String content;
    private Instant createdAt;

    public static ChatHistoryItem from(ChatMessage m) {
        return ChatHistoryItem.builder()
                .role(m.getRole()).content(m.getContent()).createdAt(m.getCreatedAt())
                .build();
    }
}
