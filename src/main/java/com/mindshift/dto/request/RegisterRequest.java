package com.mindshift.dto.request;

import com.mindshift.model.enums.Goal;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class RegisterRequest {
    @NotBlank(message = "Name is required")
    @Size(max = 100)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @Min(0) @Max(10)
    private Integer severity = 9;

    private Goal goal = Goal.SUPERHUMAN;

    private List<String> impacts = List.of();
}
