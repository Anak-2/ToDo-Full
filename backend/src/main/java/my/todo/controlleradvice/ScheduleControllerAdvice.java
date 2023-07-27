package my.todo.controlleradvice;

import my.todo.global.error.notfoundException.ScheduleNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ScheduleControllerAdvice {
    @ExceptionHandler(ScheduleNotFoundException.class)
    @ResponseBody
    public ResponseEntity<?> handleScheduleNotFoundException(ScheduleNotFoundException notFoundException){
        return new ResponseEntity<>(notFoundException.getMessage(), HttpStatus.NOT_FOUND);
    }
}
