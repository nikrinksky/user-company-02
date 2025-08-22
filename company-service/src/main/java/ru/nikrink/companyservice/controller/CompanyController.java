package ru.nikrink.companyservice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.nikrink.companyservice.dto.CompanyDTO;
import ru.nikrink.companyservice.dto.CompanyRequestDTO;
import ru.nikrink.companyservice.dto.CompanyResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.nikrink.companyservice.service.CompanyService;


@Slf4j
@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

    // Временно для теста.
    @GetMapping("/test")
    public String getAllCompanies_test() {
        return "Hallo company";
    }


    // Получить все компании (без сотрудников)
    @GetMapping
    public Page<CompanyResponseDTO> getAllCompanies(Pageable pageable) {
        log.info("Запрошены все компании только с id компании с пагинацией: {}", pageable);
        return companyService.getAllCompanies(pageable);
    }

    // Получить все компании с сотрудниками
    @GetMapping("/with-users")
    public Page<CompanyResponseDTO> getAllCompaniesWithUsers(@PageableDefault(size = 15) Pageable pageable) {
        log.info("Запрошены все компании с сотрудниками с пагинацией: {}", pageable);
        return companyService.getAllCompaniesWithUsers(pageable);
    }

    // Получить компанию без сотрудников
    @GetMapping("/not-user/{id}")
    public CompanyResponseDTO getCompany(@PathVariable @Positive Long id) {
        log.info("Запрошена компания без сотрудников с id: {}", id);
        return companyService.getCompany(id);
    }

    // Получить компанию с сотрудниками
    @GetMapping("/{id}")
    public CompanyResponseDTO getCompanyIdWithUsers(@PathVariable @Positive Long id) {
        log.info("Запрошена компания с сотрудниками с id: {}", id);
        return companyService.getCompanyIdWithUsers(id);
    }

    // Получить компанию без сотрудников и вообще без списка сотрудников
    @GetMapping("/no-users/{id}")
    public CompanyDTO getCompanyByIdNoUsers(@PathVariable @Positive Long id) {
        log.info("Запрошена компания по id без списка сотрудников: {}", id);
        return companyService.getCompanyByIdNoUsers(id);
    }

    // Создать компанию пока без сотрудников
    @PostMapping
    public ResponseEntity<CompanyResponseDTO> createCompany(
            @Valid @RequestBody CompanyRequestDTO request) {
        CompanyResponseDTO createdCompany = companyService.createCompany(request);
        log.info("Создание новой компании: {} {}", request.name(), request.budget());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCompany);
    }

    // Обновить компанию
    @PutMapping("/{id}")
    public CompanyResponseDTO updateCompany(
            @PathVariable @Positive Long id,
            @Valid @RequestBody CompanyRequestDTO request
    ) {
        log.info("Обновление компании по ID: {}", id);
        return companyService.updateCompany(id, request);
    }

    // Удалить компанию
    @DeleteMapping("/{id}")
    public void deleteCompany(@PathVariable @Positive Long id) {
        log.info("Удаление компании по ID: {}", id);
        companyService.deleteById(id);
    }

}
