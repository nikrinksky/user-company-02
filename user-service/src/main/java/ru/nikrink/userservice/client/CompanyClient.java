package ru.nikrink.userservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.nikrink.userservice.dto.CompanyDTO;

import java.util.List;

@FeignClient(name = "company-service", url = "${company.service.url}", path = "/api/companies")
public interface CompanyClient {

    @GetMapping("/api/companies/{id}")
    CompanyDTO getCompanyById(@PathVariable Long id);

    @GetMapping("/api/companies")
    List<CompanyDTO> getAllCompanies();

}
