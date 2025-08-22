package ru.nikrink.companyservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CompanyRequestDTO(
        @NotBlank(message = "It is required to specify the company name")
        @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
        String name,
        @NotNull(message = "It is required to specify the company budget")
        @Positive(message = "Budget must be positive")
        Double budget
) {}
