package com.mindshift.dto.response;

import com.mindshift.model.enums.ThoughtCategory;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatResponse {
    private String reply;
    private ThoughtCategory detectedCategory;
}
