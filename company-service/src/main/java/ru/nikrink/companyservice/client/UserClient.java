package ru.nikrink.companyservice.client;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.nikrink.companyservice.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

//@FeignClient(name = "user-service", url = "${user.service.url}", path = "/api/users") // для локального запуска

//@FeignClient(name = "user-service", url = "http://localhost:8081", path = "/api/users") // для запуска конфигурации локально
@FeignClient(name = "user-service", url = "http://user-service:8081", path = "/api/users") // для запуска конфигурации через discovery из докера
public interface UserClient {

    @GetMapping("/company/{companyId}")
    Page<UserDTO> getUsersByCompanyId(@PathVariable Long companyId);

    @GetMapping("/api/users/{id}")
    UserDTO getUser(@PathVariable Long id);
}
