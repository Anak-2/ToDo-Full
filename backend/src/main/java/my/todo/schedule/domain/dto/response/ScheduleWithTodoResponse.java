package my.todo.schedule.domain.dto.response;

import lombok.NonNull;
import my.todo.schedule.domain.schedule.Schedule;
import my.todo.todo.domain.dto.response.TodoResponseDto;

import java.sql.Timestamp;
import java.util.List;

public class ScheduleWithTodoResponse {
    private long id;
    private Timestamp createdDate;
    @NonNull
    private String title;
    private boolean isPublic = false;
    private List<TodoResponseDto> todoResponseDtoList;

    public ScheduleWithTodoResponse(Schedule schedule, List<TodoResponseDto> todoResponseDtoList){
        this.id = schedule.getId();
        this.createdDate = schedule.getCreatedDate();
        this.title = schedule.getTitle();
        this.isPublic = schedule.isPublic();
        this.todoResponseDtoList = todoResponseDtoList;
    }
}
