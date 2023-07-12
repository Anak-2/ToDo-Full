package my.todo.schedule.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import my.todo.schedule.domain.schedule.Schedule;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class ScheduleResponseDto {

    private long id;
    private Timestamp createdDate;
    @NonNull
    private String title;
    @JsonProperty("isPublic")
    private boolean isPublic = false;

    public ScheduleResponseDto(Schedule schedule){
        this.id = schedule.getId();
        this.createdDate = schedule.getCreatedDate();
        this.title = schedule.getTitle();
        this.isPublic = schedule.isPublic();
    }
}
