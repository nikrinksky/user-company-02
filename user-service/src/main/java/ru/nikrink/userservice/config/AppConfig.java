//package ru.nikrink.userservice.config;
//
//import org.modelmapper.ModelMapper;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import ru.nikrink.userservice.dto.UserResponseDTO;
//import ru.nikrink.userservice.model.User;
//
//@Configuration
//public class AppConfig {
//
////    @Bean
////    public ModelMapper modelMapper() {
////        ModelMapper modelMapper = new ModelMapper();
////
////        // Настройка маппинга User → UserResponseDTO
////        modelMapper.typeMap(User.class, UserResponseDTO.class)
////                .addMappings(mapper -> {
////                    mapper.map(User::getId, UserResponseDTO::setId);
////                    mapper.map(User::getFirstName, UserResponseDTO::setFirstName);
////                    mapper.map(User::getLastName, UserResponseDTO::setLastName);
////                    mapper.map(User::getPhoneNumber, UserResponseDTO::setPhoneNumber);
////                    mapper.map(User::getCompanyId, UserResponseDTO::setCompanyId);
////                });
////
////        return modelMapper;
////    }
//
//    @Bean
//    public ModelMapper modelMapper() {
//        ModelMapper modelMapper = new ModelMapper();
//
//        // Настройка маппинга User → UserResponseDTO
//        modelMapper.typeMap(UserResponseDTO.class, User.class)
//                .addMappings(mapper -> {
//                    mapper.map(UserResponseDTO::getId, User::setId);
//                    mapper.map(UserResponseDTO::getFirstName, User::setFirstName);
//                    mapper.map(UserResponseDTO::getLastName, User::setLastName);
//                    mapper.map(UserResponseDTO::getPhoneNumber, User::setPhoneNumber);
//                    mapper.map(UserResponseDTO::getCompanyId, User::setCompanyId);
//                });
//
//        return modelMapper;
//    }
//}
