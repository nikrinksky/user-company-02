package ru.nikrink.userservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    @Column(name = "first_name", nullable = false, length = 50)
    @EqualsAndHashCode.Include
    private String firstName;
    @Column(name = "last_name", nullable = false, length = 50)
    @EqualsAndHashCode.Include
    private String lastName;
    @Column(name = "phone_number", nullable = false, length = 15)
    @EqualsAndHashCode.Include
    private String phoneNumber;
    @EqualsAndHashCode.Include
    private Long companyId;  // ID компании, где работает пользователь

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", companyId=" + companyId +
                '}';
    }
}
