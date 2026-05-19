package com.engineeringdigest.http_request_demo;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @Schema(description = "Full name of the user", example = "Akshit Sharma")
    private String name;

    @Min(value = 1, message = "Age must be at least 1")
    @Max(value = 70, message = "Age cannot exceed 70")
    @Schema(description = "Age of the user", example = "25")
    private int age;


}
