package my.todo.schedule.domain.dto.request;

import lombok.*;
import my.todo.member.domain.user.User;
import my.todo.schedule.domain.schedule.Schedule;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
public class ScheduleRequestDto {
        @NonNull
        private String title;
        private Long userId;
        @Builder.Default
        private boolean isPublic = false;

        public Schedule toEntity(User user){
            Schedule schedule = Schedule.builder()
                    .title(title)
                    .user(user)
                    .isPublic(isPublic)
                    .build();
            return schedule;
        }
}
