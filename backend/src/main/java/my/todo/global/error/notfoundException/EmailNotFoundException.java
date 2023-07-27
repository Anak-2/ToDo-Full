package my.todo.global.error.notfoundException;

import my.todo.global.errormsg.UserError;

public class EmailNotFoundException extends RuntimeException{
    public EmailNotFoundException(){
        super(UserError.EMAIL_NOT_FOUND.getMsg());
    }
    public EmailNotFoundException(String message) {
        super(message);
    }
}
