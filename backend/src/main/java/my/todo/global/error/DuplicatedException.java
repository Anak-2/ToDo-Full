package my.todo.global.error;

public class DuplicatedException extends RuntimeException{
    public DuplicatedException(){super("중복된 아이디 존재");}
    public DuplicatedException(String message) {
        super(message);
    }
}
