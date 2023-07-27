package my.todo.global.error.duplicatedException;


import my.todo.global.errormsg.UserError;

public class UsernameDuplicatedException extends RuntimeException{
    public UsernameDuplicatedException(){super(UserError.USERNAME_DUPLICATED.getMsg());}
    public UsernameDuplicatedException(String message) {
        super(message);
    }
}
