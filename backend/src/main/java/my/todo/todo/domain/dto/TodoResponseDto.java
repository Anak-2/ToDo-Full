package my.todo.todo.domain.dto;

import my.todo.todo.domain.todo.Todo;

import java.sql.Date;

public class TodoResponseDto {
    private Date createdDate;
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
