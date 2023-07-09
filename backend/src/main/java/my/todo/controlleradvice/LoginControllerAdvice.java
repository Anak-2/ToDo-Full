package my.todo.controlleradvice;

import lombok.extern.slf4j.Slf4j;
import my.todo.global.error.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class LoginControllerAdvice {
    @ExceptionHandler(NotAuthorizedException.class)
    @ResponseBody
    public ResponseEntity<?> handleNotAuthorizedException(NotAuthorizedException notAuthorizedException){
        return new ResponseEntity<>(notAuthorizedException.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseBody
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException notFoundException){
        return new ResponseEntity<>(notFoundException.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicatedException.class)
    @ResponseBody
    public ResponseEntity<?> handleDuplicatedException(DuplicatedException duplicatedException){
        return new ResponseEntity<>(duplicatedException.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ScheduleNotFoundException.class)
    @ResponseBody
    public ResponseEntity<?> handleScheduleNotFoundException(ScheduleNotFoundException notFoundException){
        return new ResponseEntity<>(notFoundException.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TodoNotFoundException.class)
    @ResponseBody
    public ResponseEntity<?> handleTodoNotFoundException(TodoNotFoundException notFoundException){
        return new ResponseEntity<>(notFoundException.getMessage(), HttpStatus.NOT_FOUND);
    }
}
