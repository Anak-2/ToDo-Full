package my.todo.todo.domain.dto;

import my.todo.todo.domain.todo.Todo;

import java.sql.Date;

public class TodoResponseDto {
    private Date createdDate;
    private String title;
    private String content;
    private boolean isFinished;
    public TodoResponseDto(Todo todo){

    }
}
