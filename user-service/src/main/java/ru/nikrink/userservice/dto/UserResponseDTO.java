package ru.nikrink.userservice.dto;

public record UserResponseDTO (
   Long id,
    String firstName,
    String lastName,
    String phoneNumber,
    Long companyId
    ) {}

