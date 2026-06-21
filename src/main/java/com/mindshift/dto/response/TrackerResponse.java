package com.mindshift.dto.response;

import com.mindshift.model.TrackerEntry;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrackerResponse {
    private String dayKey;
    private Integer awareness, control, presence, sleep, confidence, peace;

    public static TrackerResponse from(TrackerEntry t) {
        return TrackerResponse.builder()
                .dayKey(t.getDayKey()).awareness(t.getAwareness()).control(t.getControl())
                .presence(t.getPresence()).sleep(t.getSleep())
                .confidence(t.getConfidence()).peace(t.getPeace())
                .build();
    }
}
