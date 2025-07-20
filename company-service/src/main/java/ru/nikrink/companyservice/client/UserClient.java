package ru.nikrink.companyservice.client;

import ru.nikrink.companyservice.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
        name = "user-service",
        url = "${user.service.url:http://localhost:8081}",
        fallbackFactory = UserClientFallbackFactory.class
)
public interface UserClient {
    @GetMapping("/api/users/company/{companyId}")
    List<UserDTO> getUsersByCompanyId(@PathVariable Long companyId);
}
