package com.mindshift.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * Thin client around POST /v1/messages. Kept separate from ChatService
 * so the HTTP/contract concern (headers, payload shape, error fallback)
 * never leaks into business logic.
 */
@Service
public class GroqClientService {

    private final RestTemplate restTemplate;

    @Value("${app.groq.api-key}")
    private String apiKey;

    @Value("${app.groq.model}")
    private String model;

    @Value("${app.groq.max-tokens}")
    private int maxTokens;

    private static final String ENDPOINT = "https://api.groq.com/openai/v1/chat/completions";
    private static final String FALLBACK =
        "Try this right now: take one deep breath, hold for 4 seconds, exhale for 6. "
      + "Then tell me exactly what's going on - I want to give you the right technique.";

    public GroqClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @SuppressWarnings("unchecked")
    public String complete(String systemPrompt, List<Map<String, String>> messages) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

         /*   Map<String, Object> body = Map.of(
                "model", model,
                "max_tokens", maxTokens,
                "system", systemPrompt,
                "messages", messages
            );*/
            List<Map<String, String>> groqMessages = List.of(
                    Map.of(
                            "role", "system",
                            "content", systemPrompt
                    ),
                    Map.of(
                            "role", "user",
                            "content", messages.get(messages.size() - 1).get("content")
                    )
            );

            Map<String, Object> body = Map.of(
                    "model", model,
                    "messages", groqMessages,
                    "max_tokens", maxTokens,
                    "temperature", 0.7
            );

            System.out.println("========== GROQ DEBUG ==========");
System.out.println("API KEY = " + apiKey);
System.out.println("MODEL = " + model);
System.out.println("MAX TOKENS = " + maxTokens);
System.out.println("=====================================");
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            Map<String, Object> response = restTemplate.postForObject(ENDPOINT, entity, Map.class);

            if (response == null) return FALLBACK;
        /*    List<Map<String, Object>> content = (List<Map<String, Object>>) response.get("content");
            if (content == null || content.isEmpty()) return FALLBACK;
            return (String) content.get(0).get("text");*/
            List<Map<String, Object>> choices =
                    (List<Map<String, Object>>) response.get("choices");

            if (choices == null || choices.isEmpty())
                return FALLBACK;

            Map<String, Object> message =
                    (Map<String, Object>) choices.get(0).get("message");

            return (String) message.get("content");
        }catch (Exception e) {
    e.printStackTrace();
    return "ERROR: " + e.getMessage();
}
    }
}
