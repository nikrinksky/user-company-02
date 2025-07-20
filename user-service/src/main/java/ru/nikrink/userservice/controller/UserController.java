package ru.nikrink.userservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.nikrink.userservice.dto.UserRequestDTO;
import ru.nikrink.userservice.dto.UserResponseDTO;
import ru.nikrink.userservice.dto.UserWithCompanyDTO;
import ru.nikrink.userservice.model.User;
import ru.nikrink.userservice.repository.UserRepository;
import org.springframework.web.bind.annotation.*;
import ru.nikrink.userservice.service.UserService;

import java.util.List;
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

    @GetMapping
    public List<UserResponseDTO> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserResponseDTO getUserById(@PathVariable Long id) {
        return userService.findById(id);
    }

    //    @GetMapping("/company/{companyId}")
//    public List<UserResponseDTO> getUsersByCompanyId(@PathVariable Long companyId) {
//        return userService.findByCompanyId(companyId);
//    }
    @GetMapping("/company/{companyId}")
    public List<UserWithCompanyDTO> getUsersByCompanyId(@PathVariable Long companyId) {
        log.info("Request for users of company: {}", companyId);
        return userService.getUsersByCompanyId(companyId);
    }

    @PostMapping
    public UserResponseDTO createUser(@RequestBody UserRequestDTO userRequestDTO) {
        return userService.save(userRequestDTO);  // Принимаем DTO, а не Entity
    }

    @PutMapping("/{id}")
    public UserResponseDTO updateUser(
            @PathVariable Long id,
            @RequestBody UserRequestDTO userRequestDTO
    ) {
        return userService.update(id, userRequestDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
    }


}
