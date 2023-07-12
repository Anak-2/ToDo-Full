package my.todo.todo.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import my.todo.schedule.domain.schedule.Schedule;
import my.todo.todo.domain.todo.Todo;

import java.sql.Timestamp;

@Getter
@Builder
@AllArgsConstructor
public class TodoRequestDto {

    private String title;
    private String content;
    private boolean isFinished;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Timestamp finishDate;
    private Long scheduleId;

    public Todo toEntity(Schedule schedule){
        Todo todo = Todo.builder()
                .content(content)
                .title(title)
                .isFinished(isFinished)
                .finishDate(finishDate)
//                InvalidDataAccessApiUsageException: org.hibernate.TransientPropertyValueException 발생!
//                .toEntity() 로 생성된 Schedule 은 영속성 컨텍스트에서 꺼낸 객체가 아니기 때문이다.
//                해결 방법 -> DTO 에서 Entity 로 바꿀 때 연관관계에 있는 객체는 꼭 영속성 컨텍스트 에서 가져오도록 하자
                .schedule(schedule)
                .build();
        return todo;
    }
}
