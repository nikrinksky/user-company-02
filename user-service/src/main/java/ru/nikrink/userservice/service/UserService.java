package ru.nikrink.userservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.nikrink.userservice.dto.UserRequestDTO;
import ru.nikrink.userservice.dto.UserResponseDTO;
import ru.nikrink.userservice.dto.UserWithCompanyDTO;


public interface UserService {

    Page<UserWithCompanyDTO> getAllUsersWithCompany(Pageable pageable);
    Page<UserWithCompanyDTO> getUsersByCompanyId(Long companyId, Pageable pageable);
    Page<UserResponseDTO> findAll(Pageable pageable);
    UserResponseDTO findById(Long id);
    UserResponseDTO save(UserRequestDTO userRequestDTO);
    UserResponseDTO update(Long id, UserRequestDTO userRequestDTO);
    void deleteById(Long id);

}
