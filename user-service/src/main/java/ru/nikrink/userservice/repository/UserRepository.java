package ru.nikrink.userservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.nikrink.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findByCompanyId(Long companyId, Pageable pageable );  // Поиск пользователей по ID компании с пагинацией
    //List<User> findByCompanyId(Long companyId);  // Поиск пользователей по ID компании
}