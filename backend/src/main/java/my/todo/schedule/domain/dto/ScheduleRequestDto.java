package my.todo.schedule.domain.dto;

import lombok.*;
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
public class ScheduleRequestDto {

        private long id;
        private Date createdDate;
        @NonNull
        private String title;
        private User user;
//        ToDo: dto로 넘겨주기
        private List<TodoRequestDto> todoList;
        private boolean isPublic = false;

        @Builder
        public ScheduleRequestDto(Schedule schedule) {
            this.id = schedule.getId();
            this.createdDate = schedule.getCreatedDate();
            this.title = schedule.getTitle();
            this.user = schedule.getUser();
//            this.todoList = schedule.getTodoList().stream()
//                    .map(o -> new TodoRequestDto(o))
//                    .collect(Collectors.toList());
            this.isPublic = schedule.isPublic();
        }
}
