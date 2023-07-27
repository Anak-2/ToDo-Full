package my.todo.global.error.duplicatedException;

public class DuplicatedException extends RuntimeException{
    public DuplicatedException(String message){
        super(message);
    }
}
