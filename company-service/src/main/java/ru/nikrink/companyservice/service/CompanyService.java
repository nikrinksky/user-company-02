package ru.nikrink.companyservice.service;

import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.nikrink.companyservice.client.UserClient;
import ru.nikrink.companyservice.dto.*;
import ru.nikrink.companyservice.model.Company;
import ru.nikrink.companyservice.repository.CompanyRepository;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;

    private final UserClient userClient;  // Feign-клиент к user-service

    // Вернуть все компании (без сотрудников)
    public List<CompanyResponseDTO> getAllCompanies() {
        return companyRepository.findAll().stream()
                .map(company -> new CompanyResponseDTO(
                        company.getId(),
                        company.getName(),
                        company.getBudget(),
                        List.of() // Пока не грузим сотрудников для списка
                ))
                .toList();
    }

    // Вернуть компанию по id (без сотрудников)
    public CompanyResponseDTO getCompany(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));


        return new CompanyResponseDTO(
                company.getId(),
                company.getName(),
                company.getBudget(),
                List.of()
        );
    }

    // Вернуть все компании с сотрудниками
    public List<CompanyResponseDTO> getAllCompaniesWithUsers() {
        List<Company> companies = companyRepository.findAll();
        return companies.stream()
                .map(company -> {
                    List<UserDTO> employees = userClient.getUsersByCompanyId(company.getId());
                    return new CompanyResponseDTO(
                            company.getId(),
                            company.getName(),
                            company.getBudget(),
                            employees
                    );
                })
                .toList();
    }

    // Вернуть компанию с сотрудниками
    public CompanyResponseDTO getCompanyWithUsers(Long id) {
        try {
            Company company = companyRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Company not found"));
            if (company == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found");
            }
            List<UserDTO> employees = Collections.emptyList();
            try {
                employees = userClient.getUsersByCompanyId(id);
            } catch (FeignException e) {
                log.error("Error fetching users for company {}: {}", id, e.getMessage());
            }

            return new CompanyResponseDTO(
                    company.getId(),
                    company.getName(),
                    company.getBudget(),
                    employees
            );
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error processing request",
                    e
            );
        }
    }

    // New Вернуть компанию без сотрудников и вообще без списка сотрудников
    public CompanyDTO getCompanyByIdNoUsers(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));
        return new CompanyDTO(
                company.getId(),
                company.getName(),
                company.getBudget()
        );
    }

    // Создать компанию пока без сотрудников
    public CompanyResponseDTO createCompany(CompanyRequestDTO request) {
        Company company = new Company();
        company.setName(request.name());
        company.setBudget(request.budget());

        Company savedCompany = companyRepository.save(company);

        return new CompanyResponseDTO(
                savedCompany.getId(),
                savedCompany.getName(),
                savedCompany.getBudget(),
                List.of()  // Пока нет сотрудников
        );
    }

    // Обновить компанию
    public CompanyResponseDTO updateCompany(Long id, CompanyRequestDTO request) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));

        company.setName(request.name());
        company.setBudget(request.budget());

        Company updatedCompany = companyRepository.save(company);
        List<UserDTO> employees = userClient.getUsersByCompanyId(id);

        return new CompanyResponseDTO(
                updatedCompany.getId(),
                updatedCompany.getName(),
                updatedCompany.getBudget(),
                employees
        );
    }

    // Удалить компанию
    public void deleteById(Long id) {
        if (!companyRepository.existsById(id)) {
            throw new EntityNotFoundException("Company not found" + id);
        }
        companyRepository.deleteById(id);
    }


}