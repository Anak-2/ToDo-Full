package my.todo.global.error;

import my.todo.global.errormsg.UserError;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException() {
        super(UserError.USER_NOT_EXIST.getMsg());
    }

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotFoundException(Throwable cause) {
        super(cause);
    }

    protected UserNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
