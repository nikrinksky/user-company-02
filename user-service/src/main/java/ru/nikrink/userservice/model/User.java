package ru.nikrink.userservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(nullable = false)
    private String firstName;

//    @Column(nullable = false)
    private String lastName;

//    @Column(nullable = false, unique = true)
    private String phoneNumber;

//    @Column(nullable = false)
    private Long companyId;  // ID компании, где работает пользователь

}
