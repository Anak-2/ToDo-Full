package my.todo.todo.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import my.todo.todo.domain.todo.Todo;

import java.sql.Date;
import java.sql.Timestamp;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class TodoUpdateRequestDto {
    @NonNull
    private String title;
    private String content;
    private boolean isFinished;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Timestamp finishDate;

    public Todo toEntity(){
        Todo todo = Todo.builder()
                .content(content)
                .title(title)
                .isFinished(isFinished)
                .finishDate(finishDate)
                .build();
        return todo;
    }
}
