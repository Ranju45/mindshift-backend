package com.mindshift.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class ApiError {
    private Instant timestamp;
    private int status;
    private String message;
    private List<String> details;
}
