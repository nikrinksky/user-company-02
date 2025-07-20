package ru.nikrink.userservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

//@FeignClient(name = "company-service", url = "http://company-service:8082")
@FeignClient(name = "company-service", url = "${company.service.url}") // URL лучше вынести в конфиг
public interface CompanyClient {

    @GetMapping("/api/companies/{id}")
    CompanyDTO getCompanyById(@PathVariable Long id);

    @GetMapping("/api/companies")
    List<CompanyDTO> getAllCompanies();

    // DTO для компании
    record CompanyDTO(
            Long id,
            String name,
            Double budget
    ) {}
}
