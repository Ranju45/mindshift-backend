package com.mindshift.dto.response;

import com.mindshift.model.JournalEntry;
import com.mindshift.model.enums.ThoughtCategory;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class JournalResponse {
    private Long id;
    private String text;
    private Integer intensity;
    private List<String> tags;
    private ThoughtCategory category;
    private Instant createdAt;

    public static JournalResponse from(JournalEntry e) {
        return JournalResponse.builder()
                .id(e.getId()).text(e.getText()).intensity(e.getIntensity())
                .tags(e.getTags()).category(e.getCategory()).createdAt(e.getCreatedAt())
                .build();
    }
}
