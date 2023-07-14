package my.todo.todo.repository;

import lombok.RequiredArgsConstructor;
import my.todo.schedule.domain.schedule.Schedule;
import my.todo.todo.domain.dto.response.TodoResponseDto;
import my.todo.todo.domain.todo.Todo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CustomTodoRepository {

    private final TodoRepository todoRepository;

    public Todo getTodoById(Long todoId){
        return todoRepository.findById(todoId).orElseThrow();
    }

    public List<TodoResponseDto> getTodoListByScheduleId(Long scheduleId) {
        List<Todo> todoList = todoRepository.findByScheduleId(scheduleId);
        return todoList.stream()
                .map(TodoResponseDto::new)
                .collect(Collectors.toList());
    }

    public Todo getTodoByTitle(String title){
        return todoRepository.findByTitle(title).orElseThrow();
    }

//    저장 후 ResponseDto를 만들기 위해 getId 를 통해 방금 저장한 데이터의 PK 반환
    public Long save(Todo todo){
        return todoRepository.save(todo).getId();
    }

    public List<TodoResponseDto> findAll(){
        List<Todo> todoList = todoRepository.findAll();
        return todoList.stream()
                .map(TodoResponseDto::new)
                .collect(Collectors.toList());
    }

    public void delete(Todo todo) {
        todoRepository.delete(todo);
    }
}
