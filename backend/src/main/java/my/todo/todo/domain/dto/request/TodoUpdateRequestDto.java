package my.todo.todo.domain.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import my.todo.todo.domain.todo.Todo;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TodoUpdateRequestDto {
    @NonNull
    private Long todoId;
    private String title;
    private String content;
    private boolean isFinished;

    public Todo toEntity(){
        Todo todo = Todo.builder()
                .content(content)
                .title(title)
                .isFinished(isFinished)
                .build();
        return todo;
    }
}
