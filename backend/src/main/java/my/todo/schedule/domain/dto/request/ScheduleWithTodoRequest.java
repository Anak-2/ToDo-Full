package my.todo.schedule.domain.dto.request;

import lombok.NonNull;
import my.todo.member.domain.user.User;
import my.todo.schedule.domain.schedule.Schedule;

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
