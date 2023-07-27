package my.todo.global.error.notfoundException;

import my.todo.global.errormsg.TodoError;

public class TodoNotFoundException extends RuntimeException{
    public TodoNotFoundException(){
        super(TodoError.TODO_NOT_FOUND.getMsg());
    }
    public TodoNotFoundException(String message) {
        super(message);
    }
}
