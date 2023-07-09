package my.todo.schedule.domain.dto.request;

import lombok.*;
import my.todo.member.domain.user.User;
import my.todo.schedule.domain.schedule.Schedule;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class ScheduleUpdateRequestDto {
    @NonNull
    private String title;
    private Long id;
    @Builder.Default
    private boolean isPublic = false;

    public Schedule toEntity(){
        Schedule schedule = Schedule.builder()
                .title(title)
                .id(id)
                .isPublic(isPublic)
                .build();
        return schedule;
    }
}
