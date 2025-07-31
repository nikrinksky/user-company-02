package ru.nikrink.companyservice.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.nikrink.companyservice.dto.CompanyDTO;
import ru.nikrink.companyservice.dto.CompanyRequestDTO;
import ru.nikrink.companyservice.dto.CompanyResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.nikrink.companyservice.service.CompanyService;

import java.util.List;

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
    public List<CompanyResponseDTO> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    // Получить все компании с сотрудниками
    @GetMapping("/with-users")
    public List<CompanyResponseDTO> getAllCompaniesWithUsers() {
        return companyService.getAllCompaniesWithUsers();
    }

    // Получить компанию без сотрудников
    @GetMapping("/not-user/{id}")
    public CompanyResponseDTO getCompany(@PathVariable Long id) {
        return companyService.getCompany(id);
    }

    // Получить компанию с сотрудниками
    @GetMapping("/{id}")
    public CompanyResponseDTO getCompanyWithUsers(@PathVariable Long id) {
        return companyService.getCompanyWithUsers(id);
    }

    // New вернуть компанию без сотрудников и вообще без списка сотрудников
    @GetMapping("/no-users/{id}")
    public CompanyDTO getCompanyByIdNoUsers(@PathVariable Long id) {
        return companyService.getCompanyByIdNoUsers(id);
    }

    // Создать компанию пока без сотрудников
    @PostMapping
    public ResponseEntity<CompanyResponseDTO> createCompany(
            @Valid @RequestBody CompanyRequestDTO request) {
        CompanyResponseDTO createdCompany = companyService.createCompany(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCompany);
    }

    // Обновить компанию
    @PutMapping("/{id}")
    public CompanyResponseDTO updateCompany(
            @PathVariable Long id,
            @RequestBody CompanyRequestDTO request
    ) {
        return companyService.updateCompany(id, request);
    }

    // Удалить компанию
    @DeleteMapping("/{id}")
    public void deleteCompany(@PathVariable Long id) {
        companyService.deleteById(id);
    }

}
