package com.mindshift.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class JournalRequest {
    @NotBlank(message = "Journal text cannot be empty")
    private String text;

    @Min(0) @Max(10)
    private Integer intensity = 5;

    private List<String> tags = List.of();
}
