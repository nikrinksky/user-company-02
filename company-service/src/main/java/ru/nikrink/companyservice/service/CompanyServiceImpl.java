package ru.nikrink.companyservice.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.nikrink.companyservice.client.UserClient;
import ru.nikrink.companyservice.dto.*;
import ru.nikrink.companyservice.exception.CompanyNotFoundException;
import ru.nikrink.companyservice.mapper.CompanyMapper;
import ru.nikrink.companyservice.model.Company;
import ru.nikrink.companyservice.repository.CompanyRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final UserClient userClient;  // Feign-клиент к user-service
    private final CompanyMapper companyMapper;

    // Вернуть все компании (без сотрудников)
    public Page<CompanyResponseDTO> getAllCompanies(Pageable pageable) {
        Page<Company> companyPage = companyRepository.findAll(pageable);
        List<CompanyResponseDTO> content = companyPage
                .map(company -> new CompanyResponseDTO(
                        company.getId(),
                        company.getName(),
                        company.getBudget(),
                        Page.empty()  // Пока не грузим сотрудников для списка
                ))
                .toList();
        Page<CompanyResponseDTO> page = new PageImpl<>(content, pageable, companyPage.getTotalElements());
        log.info("Запросили все компании без сотрудников");
        return page;
    }

    // Вернуть компанию по id (без сотрудников)
    public CompanyResponseDTO getCompany(Long id) {
        Company company = getCompanyOrThrow(id);
        log.info("Запросили компанию по ID без сотрудников: {}", id);
        return companyMapper.mapToResponseDTO(company);
    }

//     Вернуть все компании с сотрудниками
    public Page<CompanyResponseDTO> getAllCompaniesWithUsers(Pageable pageable) {
        Page<Company> companies = companyRepository.findAll(pageable);
        List<CompanyResponseDTO> content = companies.stream()
                .map(company -> {
                    Page<UserDTO> employees = userClient.getUsersByCompanyId(company.getId());
                    return new CompanyResponseDTO(
                            company.getId(),
                            company.getName(),
                            company.getBudget(),
                            employees
                    );
                })
                .toList();
        Page<CompanyResponseDTO> page = new PageImpl<>(content, pageable, companies.getTotalElements());
        log.info("Fetched {} users with company data", page.getNumberOfElements());
        return page;
    }

    // Вернуть компанию с сотрудниками
    public CompanyResponseDTO getCompanyIdWithUsers(Long id) {
        try {
            Company company = getCompanyOrThrow(id);
            Page<UserDTO> employees =null;
            try {
                employees = userClient.getUsersByCompanyId(id);
            } catch (FeignException e) {
                log.error("Error fetching users for company {}: {}", id, e.getMessage());
            }
            log.info("Запросили компанию с ID и сотрудниками: {}", id);
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

    //Вернуть компанию без сотрудников и вообще без списка сотрудников
    public CompanyDTO getCompanyByIdNoUsers(Long id) {
        Company company = getCompanyOrThrow(id);
        log.info("Запросили компанию без сотрудников по ID: {}", id);
        return new CompanyDTO(
                company.getId(),
                company.getName(),
                company.getBudget()
        );
    }

    // Создать компанию пока без сотрудников
    public CompanyResponseDTO createCompany(CompanyRequestDTO request) {
        Company company = companyMapper.toEntity(request);
        log.info("Перед сохранением {}", company);
        Company savedCompany = companyRepository.save(company);
        log.info("После сохранения. ID: {}, Компания пока без сотрудников: {}", savedCompany.getId(), savedCompany);
        return companyMapper.mapToResponseDTO(savedCompany);
    }

    // Обновить компанию
    public CompanyResponseDTO updateCompany(Long id, CompanyRequestDTO request) {
        Company company = getCompanyOrThrow(id);
                companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found"));

        company.setName(request.name());
        company.setBudget(request.budget());

        Company updatedCompany = companyRepository.save(company);
        Page<UserDTO> employees = userClient.getUsersByCompanyId(id);
        log.info("Обновление компании по ID с данными о пользователях: {}", id);
        return new CompanyResponseDTO(
                updatedCompany.getId(),
                updatedCompany.getName(),
                updatedCompany.getBudget(),
                employees
        );
    }

    // Удалить компанию
    public void deleteById(Long id) {
        getCompanyOrThrow(id);
        log.info("Удаление компанию с ID: {}", id);
        companyRepository.deleteById(id);
    }

    private Company getCompanyOrThrow(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Компания с id {} не найдена", id);
                    return new CompanyNotFoundException("Компания с идентификатором " + id + " не найдена");
                });
    }

}