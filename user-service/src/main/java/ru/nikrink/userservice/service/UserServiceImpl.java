package ru.nikrink.userservice.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nikrink.userservice.client.CompanyClient;
import ru.nikrink.userservice.dto.CompanyDTO;
import ru.nikrink.userservice.dto.UserRequestDTO;
import ru.nikrink.userservice.dto.UserResponseDTO;
import ru.nikrink.userservice.dto.UserWithCompanyDTO;
import ru.nikrink.userservice.exception.UserNotFoundException;
import ru.nikrink.userservice.mapper.UserMapper;
import ru.nikrink.userservice.model.User;
import ru.nikrink.userservice.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CompanyClient companyClient;
    private final UserMapper userMapper;


    // Возвращаем всех пользователей и данные их компаний
    @Override
    public Page<UserWithCompanyDTO> getAllUsersWithCompany(Pageable pageable) {

        Page<User> userPage = userRepository.findAll(pageable);

        List<UserWithCompanyDTO> content = userPage.stream()
                .map(user -> {
                    CompanyDTO company;
                    try {
                        company = companyClient.getCompanyById(user.getCompanyId());
                    } catch (FeignException e) {
                        log.error("Ошибка при выборе компании {}", e.getMessage());
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

        Page<UserWithCompanyDTO> page = new PageImpl<>(content, pageable, userPage.getTotalElements());
        log.info("Запросили всех {} пользователей с данными о компаниях", page.getNumberOfElements());
        return page;
    }

    // Возвращаем пользователей одной компании с данными о компании с пагинацией
    @Override
    public Page<UserWithCompanyDTO> getUsersByCompanyId(Long companyId, Pageable pageable) {

        Page<User> userPage = userRepository.findByCompanyId(companyId, pageable);

        List<UserWithCompanyDTO> content = userPage.stream()
                .map(user -> {
                    CompanyDTO company;
                    try {

                        company = companyClient.getCompanyByIdNoUsers(companyId);  // Вызов Feign-клиента
                    } catch (FeignException e) {
                        log.error("Ошибка при выборе компании: {}", e.getMessage());
                        company = new CompanyDTO(null, "Unknown", 0.0);  // Fallback
                    }

                    return new UserWithCompanyDTO(
                            user.getId(),
                            user.getFirstName(),
                            user.getLastName(),
                            user.getPhoneNumber(),
                            company
                    );
                })
                .filter(userWithCompanyDTO -> userWithCompanyDTO.company().id().equals(companyId))
                .toList();

        Page<UserWithCompanyDTO> page = new PageImpl<>(content, pageable, userPage.getTotalElements());
        log.info("Запросили {} пользователей с данными о компании", page.getNumberOfElements());
        return page;
    }

    // Возвращаем всех пользователей только с id компании
    @Override
    public Page<UserResponseDTO> findAll(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        log.info("Запросили всех {} пользователей только с id компании", users.getNumberOfElements());
        return users.map(userMapper::mapToResponseDTO);
    }

    // Возвращаем пользователя только с id компании
    @Override
    public UserResponseDTO findById(Long id) {
        User user = getUserOrThrow(id);
        log.info("Запросили пользователя с id: {} без id компании", id);
        return userMapper.mapToResponseDTO(user);
    }

    // Создаем пользователя
    @Override
    @Transactional
    public UserResponseDTO save(UserRequestDTO userRequestDTO) {
        User user = userMapper.toEntity(userRequestDTO);
        log.info("Перед сохранением {}", user);
        User savedUser = userRepository.save(user);
        log.info("После сохранения. ID: {}, Пользователь: {}", savedUser.getId(), savedUser);
        return userMapper.mapToResponseDTO(savedUser);

    }

    // Обновляем пользователя
    @Override
    @Transactional
    public UserResponseDTO update(Long id, UserRequestDTO userRequestDTO) {

        User existingUser = getUserOrThrow(id);

        existingUser.setFirstName(userRequestDTO.firstName());
        existingUser.setLastName(userRequestDTO.lastName());
        existingUser.setPhoneNumber(userRequestDTO.phoneNumber());
        existingUser.setCompanyId(userRequestDTO.companyId());

        User updatedUser = userRepository.save(existingUser);
        log.info("Обновление пользователя с ID: {}", id);
        return userMapper.mapToResponseDTO(updatedUser);
    }

    // Удаляем пользователя
    @Override
    public void deleteById(Long id) {
        getUserOrThrow(id);
        log.info("Удаление пользователя с ID: {}", id);
        userRepository.deleteById(id);
    }

    private User getUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Пользователь с id {} не найден", id);;
                    return new UserNotFoundException("Пользователь с идентификатором " + id + " не найден");
                });
    }
}
