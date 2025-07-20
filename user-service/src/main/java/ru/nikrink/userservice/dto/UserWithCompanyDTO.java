package ru.nikrink.userservice.dto;

import ru.nikrink.userservice.client.CompanyClient;

    public record UserWithCompanyDTO(
            Long id,
            String firstName,
            String lastName,
            String phoneNumber,
            CompanyClient.CompanyDTO company  // Вложенный объект компании!
    ) {}

//    // DTO для данных компании (можно использовать record из CompanyClient)
//    public record CompanyData(
//            Long id,
//            String name,
//            Double budget
//    ) {}


