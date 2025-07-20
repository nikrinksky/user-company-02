package ru.nikrink.companyservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "companies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double budget;

//    @ElementCollection  // Список ID пользователей (хранится в отдельной таблице)
//    private List<Long> employeeIds = new ArrayList<>();

}
