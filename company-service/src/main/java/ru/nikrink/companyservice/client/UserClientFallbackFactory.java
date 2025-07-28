package ru.nikrink.companyservice.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;


@Slf4j
@Component
public class UserClientFallbackFactory implements FallbackFactory<UserClient> {

    @Override
    public UserClient create(Throwable cause) {
        return companyId -> {
            log.error("Fallback triggered for company: {}", companyId, cause);
            return Collections.emptyList(); // Просто возвращаем пустой список
        };
    }
}
