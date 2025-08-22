package ru.nikrink.companyservice.dto;

import org.springframework.data.domain.Page;

public record CompanyResponseDTO(
        Long id,
        String name,
        Double budget,
        Page<UserDTO> employees  // Список пользователей (не ID!)
) {}
