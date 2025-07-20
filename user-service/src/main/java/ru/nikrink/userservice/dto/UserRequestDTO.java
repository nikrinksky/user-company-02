package ru.nikrink.userservice.dto;

public record UserRequestDTO(
        String firstName,
        String lastName,
        String phoneNumber,
        Long companyId
) {}
