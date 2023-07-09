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

    public List<TodoResponseDto> getTodoListBySchedule(Schedule schedule) {
        List<Todo> todoList = todoRepository.findBySchedule(schedule);
        return todoList.stream()
                .map(TodoResponseDto::new)
                .collect(Collectors.toList());
    }

    public Todo getTodoByTitle(String title){
        return todoRepository.findByTitle(title).orElseThrow();
    }

    public void save(Todo todo){
        todoRepository.save(todo);
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
