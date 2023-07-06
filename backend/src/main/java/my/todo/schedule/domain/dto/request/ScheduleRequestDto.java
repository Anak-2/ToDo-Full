package my.todo.schedule.domain.dto.request;

import lombok.*;
import my.todo.member.domain.dto.UserRequestDto;
import my.todo.member.domain.user.User;
import my.todo.schedule.domain.schedule.Schedule;
import my.todo.todo.domain.dto.TodoRequestDto;
import my.todo.todo.domain.todo.Todo;

import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
