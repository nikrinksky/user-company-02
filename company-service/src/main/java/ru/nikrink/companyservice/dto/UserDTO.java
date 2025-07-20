package ru.nikrink.companyservice.dto;

import lombok.Data;

public record UserDTO(
        Long id,
        String firstName,
        String lastName,
        String phoneNumber
) {}
