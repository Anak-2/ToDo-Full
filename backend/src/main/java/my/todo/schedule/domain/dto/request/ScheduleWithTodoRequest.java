package my.todo.schedule.domain.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import my.todo.member.domain.user.User;
import my.todo.schedule.domain.schedule.Schedule;

// Schedule 을 이용해 Todo List 를 조회하기 위한 DTO
@Getter
@Builder
public class ScheduleWithTodoRequest {
    @NonNull
    private String title;
    private boolean isPublic = false;

    public Schedule toEntity(){
        Schedule schedule = Schedule.builder()
                .title(title)
                .isPublic(isPublic)
                .build();
        return schedule;
    }
}
