package ru.nikrink.companyservice.mapper;

import org.mapstruct.Mapper;
import ru.nikrink.companyservice.dto.CompanyRequestDTO;
import ru.nikrink.companyservice.dto.CompanyResponseDTO;
import ru.nikrink.companyservice.model.Company;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    CompanyResponseDTO mapToResponseDTO(Company company);
    Company toEntity(CompanyRequestDTO companyRequestDTO);

}
