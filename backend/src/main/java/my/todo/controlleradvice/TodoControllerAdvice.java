package my.todo.controlleradvice;

import my.todo.global.error.notfoundException.TodoNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class TodoControllerAdvice {
    @ExceptionHandler(TodoNotFoundException.class)
    @ResponseBody
    public ResponseEntity<?> handleTodoNotFoundException(TodoNotFoundException notFoundException){
        return new ResponseEntity<>(notFoundException.getMessage(), HttpStatus.NOT_FOUND);
    }
}
