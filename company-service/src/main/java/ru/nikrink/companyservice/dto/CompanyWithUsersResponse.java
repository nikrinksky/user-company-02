package ru.nikrink.companyservice.dto;

import lombok.Data;
import ru.nikrink.companyservice.model.Company;

import java.util.List;

@Data
public class CompanyWithUsersResponse {
    private Long id;
    private String name;
    private Double budget;
    private List<UserDTO> employees;  // Используем UserDTO вместо User

    public static CompanyWithUsersResponse fromCompany(Company company, List<UserDTO> users) {
        CompanyWithUsersResponse response = new CompanyWithUsersResponse();
        response.setId(company.getId());
        response.setName(company.getName());
        response.setBudget(company.getBudget());
        response.setEmployees(users);
        return response;
    }
}
