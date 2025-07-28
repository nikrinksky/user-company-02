package ru.nikrink.userservice.dto;


    public record UserWithCompanyDTO(
            Long id,
            String firstName,
            String lastName,
            String phoneNumber,
            CompanyDTO company  // Вложенный объект компании!
    ) {}




