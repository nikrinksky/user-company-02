package ru.nikrink.companyservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.nikrink.companyservice.dto.CompanyDTO;
import ru.nikrink.companyservice.dto.CompanyRequestDTO;
import ru.nikrink.companyservice.dto.CompanyResponseDTO;

public interface CompanyService {

    Page<CompanyResponseDTO> getAllCompanies(Pageable pageable);
    CompanyResponseDTO getCompany(Long id);
    Page<CompanyResponseDTO> getAllCompaniesWithUsers(Pageable pageable);
    CompanyResponseDTO getCompanyIdWithUsers(Long id);
    CompanyDTO getCompanyByIdNoUsers(Long id);
    CompanyResponseDTO createCompany(CompanyRequestDTO request);
    CompanyResponseDTO updateCompany(Long id, CompanyRequestDTO request);
    void deleteById(Long id);

}