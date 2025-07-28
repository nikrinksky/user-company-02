package ru.nikrink.companyservice.dto;


public record UserDTO(
        Long id,
        String firstName,
        String lastName,
        String phoneNumber
) {}
