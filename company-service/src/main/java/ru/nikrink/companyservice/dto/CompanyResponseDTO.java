package ru.nikrink.companyservice.dto;

import java.util.List;

public record CompanyResponseDTO(
        Long id,
        String name,
        Double budget,
        List<UserDTO> employees  // Список пользователей (не ID!)
) {}
