package com.mindshift.service;

import com.mindshift.dto.request.ChatRequest;
import com.mindshift.dto.response.ChatHistoryItem;
import com.mindshift.dto.response.ChatResponse;
import com.mindshift.model.User;

import java.util.List;

public interface ChatService {
    ChatResponse sendMessage(User user, ChatRequest request);
    List<ChatHistoryItem> getHistory(User user);
}
