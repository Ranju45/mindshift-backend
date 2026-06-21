package com.mindshift.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TriggerWordResponse {
    private String word;
    private int frequency;
}
