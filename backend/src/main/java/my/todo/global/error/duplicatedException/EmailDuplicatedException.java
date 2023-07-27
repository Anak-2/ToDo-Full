package my.todo.global.error.duplicatedException;

import my.todo.global.errormsg.UserError;

public class EmailDuplicatedException extends RuntimeException{
    public EmailDuplicatedException(){super(UserError.EMAIL_DUPLICATED.getMsg());}
    public EmailDuplicatedException(String message){
        super(message);
    }
}
