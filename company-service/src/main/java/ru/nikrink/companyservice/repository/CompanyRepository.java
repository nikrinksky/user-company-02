package ru.nikrink.companyservice.repository;

import ru.nikrink.companyservice.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    // Можно добавить кастомные запросы, например:
    // Company findByName(String name);
}