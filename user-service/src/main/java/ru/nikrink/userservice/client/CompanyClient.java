package ru.nikrink.userservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.nikrink.userservice.dto.CompanyDTO;



//@FeignClient(name = "company-service", path = "/api/companies", url = "http://localhost:8082")
@FeignClient(name = "company-service", path = "/api/companies", url = "http://company-service:8082")
public interface CompanyClient {

    @GetMapping("/{id}")
    CompanyDTO getCompanyById(@PathVariable Long id);

    //
    @GetMapping("/no-users/{id}")
    CompanyDTO getCompanyByIdNoUsers(@PathVariable Long id);

}
