package my.todo.global.error;

public class DuplicatedException extends RuntimeException{
    public DuplicatedException(String message) {
        super(message);
    }
}
