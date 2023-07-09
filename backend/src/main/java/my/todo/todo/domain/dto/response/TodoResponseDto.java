package my.todo.todo.domain.dto.response;

import lombok.Getter;
import my.todo.todo.domain.todo.Todo;

import java.sql.Date;
import java.sql.Timestamp;

@Getter
public class TodoResponseDto {
    private Timestamp createdDate;
    private String title;
    private String content;
    private boolean isFinished;

    public TodoResponseDto(Todo todo){
        this.createdDate = todo.getCreatedDate();
        this.title = todo.getTitle();
        this.content = todo.getContent();
        this.isFinished = todo.isFinished();
    }
}
