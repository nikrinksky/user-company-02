package ru.nikrink.userservice.service;

import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.nikrink.userservice.client.CompanyClient;
import ru.nikrink.userservice.dto.CompanyDTO;
import ru.nikrink.userservice.dto.UserRequestDTO;
import ru.nikrink.userservice.dto.UserResponseDTO;
import ru.nikrink.userservice.dto.UserWithCompanyDTO;
import ru.nikrink.userservice.model.User;
import ru.nikrink.userservice.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CompanyClient companyClient;


    // Возвращаем всех пользователей и данные их компаний
    public List<UserWithCompanyDTO> getAllUsersWithCompany() {
        List<User> users = userRepository.findAll();
        log.info("Found users: {}", users.size());

        return users.stream()
                .map(user -> {
                    CompanyDTO company = null;
                    try {
                        company = companyClient.getCompanyById(user.getCompanyId());
                    } catch (FeignException e) {
                        log.error("Error fetching company: {}", e.getMessage());
                        company = new CompanyDTO(null, "Пока не работает ни в одной компании", 0.0);  // Fallback
                    }
                    return new UserWithCompanyDTO(
                            user.getId(),
                            user.getFirstName(),
                            user.getLastName(),
                            user.getPhoneNumber(),
                            company
                    );
                })
                .toList();

    }


    // Возвращаем пользователей одной компании с данными о компании
    public List<UserWithCompanyDTO> getUsersByCompanyId(Long companyId) {
        // 1. Асинхронно получаем пользователей и данные компании
        List<User> users = userRepository.findByCompanyId(companyId);
        log.info("Found users: {}", users.size());

        CompanyDTO company;
        try {
            // New временно убрал getCompanyById в вызове Feign-клиента
            company = companyClient.getCompanyByIdNoUsers(companyId);  // Вызов Feign-клиента
        } catch (FeignException e) {
            log.error("Error fetching company: {}", e.getMessage());
            company = new CompanyDTO(null, "Unknown", 0.0);  // Fallback
        }

        // 2. Преобразуем данные
        CompanyDTO finalCompany = company;
        return users.stream()
                .map(user -> new UserWithCompanyDTO(
                        user.getId(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getPhoneNumber(),
                        finalCompany
                ))
                .toList();
    }

    // Возвращаем всех пользователей только с id компании
    public List<UserResponseDTO> findAll() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    // Возвращаем пользователя только с id компании
    public UserResponseDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return convertToDTO(user);
    }

    // Создаем пользователя
    public UserResponseDTO save(UserRequestDTO userRequestDTO) {
        // Преобразуем DTO в Entity
        User user = convertToEntity(userRequestDTO);
        log.info("Before save: {}", user);  // Проверьте поля перед сохранением
        User savedUser = userRepository.save(user);
        log.info("After save. ID: {}, User: {}", savedUser.getId(), savedUser);  // Проверьте ID
        return convertToDTO(savedUser);

    }

    // Обновляем пользователя
    public UserResponseDTO update(Long id, UserRequestDTO userRequestDTO) {
        // 1. Находим пользователя
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        // 2. Обновляем данные вручную
        existingUser.setFirstName(userRequestDTO.firstName());
        existingUser.setLastName(userRequestDTO.lastName());
        existingUser.setPhoneNumber(userRequestDTO.phoneNumber());
        existingUser.setCompanyId(userRequestDTO.companyId());

        // 3. Сохраняем
        User updatedUser = userRepository.save(existingUser);

        // 4. Возвращаем DTO
        return convertToDTO(updatedUser);
    }

    // Удаляем пользователя
    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    // Используется для преобразования User → UserResponseDTO
    private UserResponseDTO convertToDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getCompanyId()
        );
    }

    // Используется для преобразования UserRequestDTO → User
    private User convertToEntity(UserRequestDTO dto) {
        User user = new User();
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setPhoneNumber(dto.phoneNumber());
        user.setCompanyId(dto.companyId());
        return user;
    }

}
