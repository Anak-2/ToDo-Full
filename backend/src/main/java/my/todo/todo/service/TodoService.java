package my.todo.todo.service;

import lombok.RequiredArgsConstructor;
import my.todo.schedule.domain.dto.request.ScheduleWithTodoRequest;
import my.todo.schedule.domain.schedule.Schedule;
import my.todo.schedule.repository.CustomScheduleRepository;
import my.todo.todo.domain.dto.request.TodoRequestDto;
import my.todo.todo.domain.dto.request.TodoUpdateRequestDto;
import my.todo.todo.domain.dto.response.TodoResponseDto;
import my.todo.todo.domain.todo.Todo;
import my.todo.todo.repository.CustomTodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final CustomTodoRepository customTodoRepository;
    private final CustomScheduleRepository customScheduleRepository;

//    query todo by todo title
    public TodoResponseDto getTodo(String title){
        return new TodoResponseDto(customTodoRepository.getTodoByTitle(title));
    }

//    query todo list by schedule
    public List<TodoResponseDto> getTodosBySchedule(ScheduleWithTodoRequest scheduleWithTodoRequest){
        customTodoRepository.getTodoListBySchedule(scheduleWithTodoRequest.toEntity());
        return null;
    }

//    add todo
    public void addTodo(TodoRequestDto todoRequestDto){
        Schedule findSchedule = customScheduleRepository.getScheduleById(todoRequestDto.getScheduleId());
        customTodoRepository.save(todoRequestDto.toEntity(findSchedule));
    }

    public void deleteTodo(Long todoId) {
        Todo todo = customTodoRepository.getTodoById(todoId);
        customTodoRepository.delete(todo);
    }

    public void updateTodo(TodoUpdateRequestDto todoUpdateRequestDto) {
        Todo todo = customTodoRepository.getTodoById(todoUpdateRequestDto.getTodoId());
        todo.updateTitle(todoUpdateRequestDto.getTitle());
        todo.updateContent(todoUpdateRequestDto.getContent());
        todo.updateIsFinished(todoUpdateRequestDto.isFinished());
    }
}
