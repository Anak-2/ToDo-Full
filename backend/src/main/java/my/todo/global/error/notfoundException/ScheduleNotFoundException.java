package my.todo.global.error.notfoundException;


import my.todo.global.errormsg.ScheduleError;

public class ScheduleNotFoundException extends RuntimeException{
    public ScheduleNotFoundException(){
        super(ScheduleError.SCHEDULE_NOT_FOUND.getMsg());
    }
    public ScheduleNotFoundException(String message) {
        super(message);
    }
}
