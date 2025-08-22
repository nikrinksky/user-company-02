package ru.nikrink.userservice.mapper;


import org.mapstruct.Mapper;
import ru.nikrink.userservice.dto.UserRequestDTO;
import ru.nikrink.userservice.dto.UserResponseDTO;
import ru.nikrink.userservice.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDTO mapToResponseDTO( User user);
    User toEntity(UserRequestDTO userRequestDTO);
}
