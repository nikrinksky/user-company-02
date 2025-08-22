package ru.nikrink.userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleUserNotFoundException(UserNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleAlreadyExists(UserAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Пользователь с таким именем уже существует");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        ex.printStackTrace(); // Логируем исключение
        return ResponseEntity.status(500).body(ex.getMessage());
    }

    //    @ExceptionHandler(UserNotFoundException.class)
//    public ResponseEntity<String> handleNotFound(UserNotFoundException e) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь с идентификатором \" + id + \" не найден");
//    }
}
