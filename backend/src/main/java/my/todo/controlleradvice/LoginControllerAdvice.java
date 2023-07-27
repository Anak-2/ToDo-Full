package my.todo.controlleradvice;

import lombok.extern.slf4j.Slf4j;
import my.todo.global.error.*;
import my.todo.global.error.duplicatedException.EmailDuplicatedException;
import my.todo.global.error.duplicatedException.UsernameDuplicatedException;
import my.todo.global.error.notfoundException.EmailNotFoundException;
import my.todo.global.error.notfoundException.ScheduleNotFoundException;
import my.todo.global.error.notfoundException.TodoNotFoundException;
import my.todo.global.error.notfoundException.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @ExceptionHandler(UsernameDuplicatedException.class)
    @ResponseBody
    public ResponseEntity<?> handleUsernameDuplicatedException(UsernameDuplicatedException duplicatedException){
        return new ResponseEntity<>(duplicatedException.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailDuplicatedException.class)
    @ResponseBody
    public ResponseEntity<?> handleEmailDuplicatedException(EmailDuplicatedException duplicatedException){
        return new ResponseEntity<>(duplicatedException.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailNotFoundException.class)
    @ResponseBody
    public ResponseEntity<?> handleEmailNotFoundException(EmailNotFoundException emailNotFoundException){
        return new ResponseEntity<>(emailNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
