package ru.nikrink.userservice.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import ru.nikrink.userservice.dto.CompanyDTO;

import java.util.List;

@Slf4j
@Component
public class CompanyClientFallbackFactory implements FallbackFactory<CompanyClient> {

    @Override
    public CompanyClient create(Throwable cause) {
        return new CompanyClient() {
            @Override
            public List<CompanyDTO> getAllCompanies() {
                return List.of();
            }

            @Override
            public CompanyDTO getCompanyById(Long id) {
                log.error("Failed to get company with id: {}", id, cause);
                return new CompanyDTO(id, "N/A", 0.0);
            }
        };
    }
}
