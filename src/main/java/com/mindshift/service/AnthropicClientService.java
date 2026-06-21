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
public class AnthropicClientService {

    private final RestTemplate restTemplate;

    @Value("${app.anthropic.api-key}")
    private String apiKey;

    @Value("${app.anthropic.model}")
    private String model;

    @Value("${app.anthropic.max-tokens}")
    private int maxTokens;

    private static final String ENDPOINT = "https://api.anthropic.com/v1/messages";
    private static final String FALLBACK =
        "Try this right now: take one deep breath, hold for 4 seconds, exhale for 6. "
      + "Then tell me exactly what's going on - I want to give you the right technique.";

    public AnthropicClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @SuppressWarnings("unchecked")
    public String complete(String systemPrompt, List<Map<String, String>> messages) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-api-key", apiKey);
            headers.set("anthropic-version", "2023-06-01");

            Map<String, Object> body = Map.of(
                "model", model,
                "max_tokens", maxTokens,
                "system", systemPrompt,
                "messages", messages
            );

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            Map<String, Object> response = restTemplate.postForObject(ENDPOINT, entity, Map.class);

            if (response == null) return FALLBACK;
            List<Map<String, Object>> content = (List<Map<String, Object>>) response.get("content");
            if (content == null || content.isEmpty()) return FALLBACK;
            return (String) content.get(0).get("text");
        } catch (Exception e) {
            return FALLBACK;
        }
    }
}
