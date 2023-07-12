package my.todo.schedule.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import my.todo.member.domain.user.User;
import my.todo.schedule.domain.schedule.Schedule;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class ScheduleUpdateRequestDto {
    @NonNull
    private String title;
    private Long id;
    @JsonProperty("isPublic")
    private boolean isPublic;

    public Schedule toEntity(){
        Schedule schedule = Schedule.builder()
                .title(title)
                .id(id)
                .isPublic(isPublic)
                .build();
        return schedule;
    }
}
