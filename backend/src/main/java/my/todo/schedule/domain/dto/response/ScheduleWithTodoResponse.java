package my.todo.schedule.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NonNull;
import my.todo.schedule.domain.schedule.Schedule;
import my.todo.todo.domain.dto.response.TodoResponseDto;

import java.sql.Timestamp;
import java.util.List;

@Getter
public class ScheduleWithTodoResponse {
    private long id;
    @NonNull
    private String title;
    @JsonProperty("isPublic")
    private boolean isPublic = false;
    private List<TodoResponseDto> todoResponseDtoList;

    public ScheduleWithTodoResponse(Schedule schedule, List<TodoResponseDto> todoResponseDtoList){
        this.id = schedule.getId();
        this.title = schedule.getTitle();
        this.isPublic = schedule.isPublic();
        this.todoResponseDtoList = todoResponseDtoList;
    }
}
