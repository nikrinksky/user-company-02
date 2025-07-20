package ru.nikrink.userservice.service;

import feign.FeignException;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.nikrink.userservice.client.CompanyClient;
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
//    private final ModelMapper modelMapper;
    private final CompanyClient companyClient;

//    public List<UserResponseDTO> findByCompanyId(Long companyId) {
//        return userRepository.findByCompanyId(companyId).stream()
//                .map(this::convertToDTO)
//                .toList();
//    }

    public List<UserWithCompanyDTO> getUsersByCompanyId(Long companyId) {
        // 1. Получаем пользователей из БД
        List<User> users = userRepository.findByCompanyId(companyId);
        log.info("Found users: {}", users.size());
        // 2. Запрашиваем данные компании из company-service
        CompanyClient.CompanyDTO company = companyClient.getCompanyById(companyId);

        // 3. Преобразуем User + CompanyDTO → UserWithCompanyDTO
        return users.stream()
                .map(user -> new UserWithCompanyDTO(
                        user.getId(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getPhoneNumber(),
                        new CompanyClient.CompanyDTO(company.id(), company.name(), company.budget())
                ))
                .toList();
    }

    public List<UserResponseDTO> findAll() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    public UserResponseDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return convertToDTO(user);
    }

    // Изменяем метод save для приёма DTO
    public UserResponseDTO save(UserRequestDTO userRequestDTO) {
        // Преобразуем DTO в Entity
        User user = convertToEntity(userRequestDTO);
        log.info("Before save: {}", user);  // Проверьте поля перед сохранением
        User savedUser = userRepository.save(user);
        log.info("After save. ID: {}, User: {}", savedUser.getId(), savedUser);  // Проверьте ID
        return convertToDTO(savedUser);

    }

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


    //  временный код для проверки Feign Client
//    @PostConstruct
//    public void init() {
//        try {
//            CompanyClient.Company company = companyClient.getCompany(1L);
//            System.out.println("Feign Client работает! Получена компания: " + company.name());
//        } catch (Exception e) {
//            System.err.println("Ошибка Feign Client: " + e.getMessage());
//        }
//    }
}
