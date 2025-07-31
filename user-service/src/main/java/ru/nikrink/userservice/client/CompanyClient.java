package ru.nikrink.userservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.nikrink.userservice.dto.CompanyDTO;



@FeignClient(name = "company-service", path = "/api/companies", url = "${company.service.url}")
public interface CompanyClient {

    @GetMapping("/{id}")
    CompanyDTO getCompanyById(@PathVariable Long id);

    //
    @GetMapping("/no-users/{id}")
    CompanyDTO getCompanyByIdNoUsers(@PathVariable Long id);

}
