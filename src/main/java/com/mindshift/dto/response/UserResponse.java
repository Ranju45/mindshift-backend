package com.mindshift.dto.response;

import com.mindshift.model.User;
import com.mindshift.model.enums.Goal;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private Integer severity;
    private Goal goal;
    private List<String> impacts;

    public static UserResponse from(User u) {
        return UserResponse.builder()
                .id(u.getId()).name(u.getName()).email(u.getEmail())
                .severity(u.getSeverity()).goal(u.getGoal()).impacts(u.getImpacts())
                .build();
    }
}
