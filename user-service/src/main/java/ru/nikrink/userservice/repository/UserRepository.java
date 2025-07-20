package ru.nikrink.userservice.repository;

import ru.nikrink.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByCompanyId(Long companyId);  // Поиск пользователей по ID компании
}