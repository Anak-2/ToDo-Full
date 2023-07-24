package my.todo.global.error;


import my.todo.global.errormsg.UserError;

public class DuplicatedException extends RuntimeException{
    public DuplicatedException(){super(UserError.DUPLICATED.getMsg());}
    public DuplicatedException(String message) {
        super(message);
    }
}
