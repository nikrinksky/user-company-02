package ru.nikrink.userservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.nikrink.userservice.dto.CompanyDTO;

import java.util.List;


//@FeignClient(name = "company-service", path = "/api/companies", url = "http://localhost:8082") // для запуска конфигурации локально
@FeignClient(name = "company-service", path = "/api/companies", url = "http://company-service:8082") // для запуска конфигурации через discovery  из докера
public interface CompanyClient {

    @GetMapping("/{id}")
    CompanyDTO getCompanyById(@PathVariable Long id);

    @GetMapping("/no-users/{id}")
    CompanyDTO getCompanyByIdNoUsers(@PathVariable Long id);

    @GetMapping
    List<CompanyDTO> getAllCompanies();

}
