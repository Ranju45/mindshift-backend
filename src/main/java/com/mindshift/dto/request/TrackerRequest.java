package com.mindshift.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TrackerRequest {
    @NotBlank
    private String dayKey;

    @Min(0) @Max(10) private Integer awareness;
    @Min(0) @Max(10) private Integer control;
    @Min(0) @Max(10) private Integer presence;
    @Min(0) @Max(10) private Integer sleep;
    @Min(0) @Max(10) private Integer confidence;
    @Min(0) @Max(10) private Integer peace;
}
