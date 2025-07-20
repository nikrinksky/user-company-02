package ru.nikrink.companyservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CompanyRequestDTO(
        @NotBlank String name,
        @Positive Double budget
) {}
