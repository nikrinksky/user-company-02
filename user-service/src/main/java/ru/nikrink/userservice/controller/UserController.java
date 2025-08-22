package ru.nikrink.userservice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import ru.nikrink.userservice.dto.UserRequestDTO;
import ru.nikrink.userservice.dto.UserResponseDTO;
import ru.nikrink.userservice.dto.UserWithCompanyDTO;
import org.springframework.web.bind.annotation.*;
import ru.nikrink.userservice.service.UserService;


@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/test")
    public String getAllUsers_test() {
        return "Hallo user";
    }

    // Получить всех пользователей только с id компании
    @GetMapping
    public Page<UserResponseDTO> getAllUsers(@PageableDefault(size = 15) Pageable pageable) {
        log.info("Получение всех пользователями только с id компании с пагинацией: {}", pageable);
        return userService.findAll(pageable);
    }

    // Получить пользователя только с id компании
    @GetMapping("/{id}")
    public UserResponseDTO getUserById(@PathVariable @Positive Long id) {
        log.info("Запрошен пользователь с ID: {} без данных о компании", id);
        return userService.findById(id);
    }

    // Получить всех пользователей одной компании с данными о компании
    @GetMapping("/company/{companyId}")
    public Page<UserWithCompanyDTO> getUsersByCompanyId(@PathVariable @Positive Long companyId, @PageableDefault(size = 11) Pageable pageable) {
        log.info("Запрос для пользователей одной компании с данными о компании с пагинацией: {}", companyId);
        return userService.getUsersByCompanyId(companyId, pageable);
    }

    // Получить всех пользователей с данными о компаниях
    @GetMapping("/company")
    public Page<UserWithCompanyDTO> getAllUsersWithCompany(@PageableDefault(size = 11) Pageable pageable) {
        log.info("Запрос для пользователей с данными о компаниях с пагинацией: {}", pageable);
        return userService.getAllUsersWithCompany(pageable);
    }

    // Создать пользователя
    @PostMapping
    public UserResponseDTO createUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        log.info("Создание нового пользователя: {} {}", userRequestDTO.firstName(), userRequestDTO.lastName());
        return userService.save(userRequestDTO);
    }

    // Обновить пользователя
    @PutMapping("/{id}")
    public UserResponseDTO updateUser(
            @PathVariable @Positive Long id,
            @Valid @RequestBody UserRequestDTO userRequestDTO
    ) {
        log.info("Обновление пользователя с ID: {}", id);
        return userService.update(id, userRequestDTO);
    }

    // Удалить пользователя
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable @Positive Long id) {
        log.info("Удаление пользователя с ID: {}", id);
        userService.deleteById(id);
    }
}
