package my.todo.global.error;


public class ScheduleNotFoundException extends RuntimeException{
    public ScheduleNotFoundException(){
        super("Schedule Not Found");
    }
    public ScheduleNotFoundException(String message) {
        super(message);
    }
}
