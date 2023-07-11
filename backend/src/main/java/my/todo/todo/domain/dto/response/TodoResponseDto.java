package my.todo.todo.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import my.todo.todo.domain.todo.Todo;

import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TodoResponseDto {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Timestamp finishDate;
    private String title;
    private String content;
    private boolean isFinished;

    public TodoResponseDto(Todo todo){
        this.finishDate = todo.getFinishDate();
        this.title = todo.getTitle();
        this.content = todo.getContent();
        this.isFinished = todo.isFinished();
    }
}
