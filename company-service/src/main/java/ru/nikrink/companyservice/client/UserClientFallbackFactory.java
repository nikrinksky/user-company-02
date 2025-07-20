package ru.nikrink.companyservice.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.nikrink.companyservice.dto.UserDTO;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class UserClientFallbackFactory implements FallbackFactory<UserClient> {

    @Override
    public UserClient create(Throwable cause) {
        return new UserClient() {
            @Override
            public List<UserDTO> getUsersByCompanyId(Long companyId) {
                log.error("Fallback triggered for company: {}", companyId, cause);
                return Collections.emptyList(); // Просто возвращаем пустой список
            }
        };
    }
}
