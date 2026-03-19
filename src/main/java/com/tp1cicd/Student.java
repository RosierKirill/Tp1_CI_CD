package com.tp1cicd;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record Student(
    Integer id,
    @NotBlank
    @Size(min = 2)
    String firstName,
    @NotBlank
    @Size(min = 2)
    String lastName,
    @NotBlank
    @Email
    String email,
    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("20.0")
    Double grade,
    @NotBlank
    @Pattern(regexp = "informatique|math\\u00E9matiques|physique|chimie")
    String field
) {
}
