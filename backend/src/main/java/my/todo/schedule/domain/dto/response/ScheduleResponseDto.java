package my.todo.schedule.domain.dto.response;

import lombok.*;
import my.todo.schedule.domain.schedule.Schedule;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleResponseDto {

    private long id;
    private Timestamp createdDate;
    @NonNull
    private String title;
    private boolean isPublic = false;

    public ScheduleResponseDto(Schedule schedule){
        this.id = schedule.getId();
        this.createdDate = schedule.getCreatedDate();
        this.title = schedule.getTitle();
        this.isPublic = schedule.isPublic();
    }
}
