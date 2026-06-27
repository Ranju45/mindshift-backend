package com.mindshift.service.impl;

import com.mindshift.dto.request.ChatRequest;
import com.mindshift.dto.response.ChatHistoryItem;
import com.mindshift.dto.response.ChatResponse;
import com.mindshift.model.ChatMessage;
import com.mindshift.model.User;
import com.mindshift.model.enums.MessageRole;
import com.mindshift.model.enums.ThoughtCategory;
import com.mindshift.repository.ChatMessageRepository;
import com.mindshift.service.GroqClientService;
import com.mindshift.service.ChatService;
import com.mindshift.util.classifier.PromptFactory;
import com.mindshift.util.classifier.ThoughtClassifierStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private static final int HISTORY_WINDOW = 10;

    private final ChatMessageRepository chatRepository;
    private final ThoughtClassifierStrategy classifier;
    private final PromptFactory promptFactory;
    private final GroqClientService groqClient;

    @Override
    @Transactional
    public ChatResponse sendMessage(User user, ChatRequest request) {
        // 1. Fast local pre-classification (Trie, O(n) per char) - used as a
        //    hint for the LLM and stored for analytics even if the AI call fails.
        ThoughtCategory category = classifier.classify(request.getMessage());

        // 2. Persist the user's message first so history is never lost.
        ChatMessage userMsg = ChatMessage.builder()
                .user(user).role(MessageRole.USER).content(request.getMessage()).build();
        chatRepository.save(userMsg);

        // 3. Build a bounded conversation window (sliding window over history,
        //    not the full table) to keep token usage and latency predictable.
        List<ChatMessage> recent = chatRepository.findByUserOrderByCreatedAtDesc(
                user, PageRequest.of(0, HISTORY_WINDOW));
        List<Map<String, String>> apiMessages = recent.stream()
                .sorted((a, b) -> a.getCreatedAt().compareTo(b.getCreatedAt()))
                .map(m -> Map.of("role", m.getRole() == MessageRole.USER ? "user" : "assistant",
                                  "content", m.getContent()))
                .toList();

        String systemPrompt = promptFactory.buildSystemPrompt(user, category);
        String reply = groqClient.complete(systemPrompt, apiMessages);

        ChatMessage aiMsg = ChatMessage.builder()
                .user(user).role(MessageRole.ASSISTANT).content(reply).build();
        chatRepository.save(aiMsg);

        return ChatResponse.builder().reply(reply).detectedCategory(category).build();
    }

    @Override
    public List<ChatHistoryItem> getHistory(User user) {
        return chatRepository.findByUserOrderByCreatedAtAsc(user)
                .stream().map(ChatHistoryItem::from).toList();
    }
}
