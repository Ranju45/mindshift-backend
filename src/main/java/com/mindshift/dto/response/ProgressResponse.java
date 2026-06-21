package com.mindshift.dto.response;

import com.mindshift.model.DailyProgress;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProgressResponse {
    private Integer currentDay;
    private List<Integer> completedDays;
    private Integer godModeScore;

    public static ProgressResponse from(DailyProgress p) {
        return ProgressResponse.builder()
                .currentDay(p.getCurrentDay())
                .completedDays(p.getCompletedDays())
                .godModeScore(p.getGodModeScore())
                .build();
    }
}
