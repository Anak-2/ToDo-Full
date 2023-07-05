package my.todo.schedule.domain.dto;

import lombok.*;
import my.todo.member.domain.dto.UserResponseDto;
import my.todo.member.domain.user.User;
import my.todo.schedule.domain.schedule.Schedule;
import my.todo.todo.domain.dto.TodoRequestDto;
import my.todo.todo.domain.dto.TodoResponseDto;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleResponseDto {

    private long id;
    private Timestamp createdDate;
    @NonNull
    private String title;
//    //        ToDo: dto로 넘겨주기
    private List<TodoResponseDto> todoList;
    private boolean isPublic = false;

    public ScheduleResponseDto(Schedule schedule){
        this.id = schedule.getId();
        this.createdDate = schedule.getCreatedDate();
        this.title = schedule.getTitle();
        this.isPublic = schedule.isPublic();
//        this.todoList = schedule.getTodoList().stream()
//                .map(todo -> new TodoResponseDto(todo))
//                .collect(Collectors.toList());
    }
}
