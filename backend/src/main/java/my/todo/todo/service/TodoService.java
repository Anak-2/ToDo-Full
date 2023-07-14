package my.todo.todo.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.todo.schedule.domain.dto.request.ScheduleRequestDto;
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
@Transactional
@Slf4j
public class TodoService {

    private final CustomTodoRepository customTodoRepository;
    private final CustomScheduleRepository customScheduleRepository;

//    query todo by todo id
    public TodoResponseDto getTodo(Long todoId){
        return new TodoResponseDto(customTodoRepository.getTodoById(todoId));
    }

//    query todo list by schedule
    public List<TodoResponseDto> getTodosByScheduleId(Long scheduleId){return customTodoRepository.getTodoListByScheduleId(scheduleId);}

//    add todo
    public Long addTodo(TodoRequestDto todoRequestDto){
        Schedule findSchedule = customScheduleRepository.getScheduleById(todoRequestDto.getScheduleId());
        return customTodoRepository.save(todoRequestDto.toEntity(findSchedule));
    }

    public void deleteTodo(Long todoId) {
        Todo todo = customTodoRepository.getTodoById(todoId);
        customTodoRepository.delete(todo);
    }

    public void updateTodo(Long todoId, TodoUpdateRequestDto todoUpdateRequestDto) {
        Todo todo = customTodoRepository.getTodoById(todoId);
        todo.updateTitle(todoUpdateRequestDto.getTitle());
        todo.updateContent(todoUpdateRequestDto.getContent());
        todo.updateIsFinished(todoUpdateRequestDto.isFinished());
        todo.updateFinishDate(todoUpdateRequestDto.getFinishDate());
//        Todo: Dirty Checking 이 안되는 이유?
//              -> @Transactional 이 안 걸려있었기 때문!
//        customTodoRepository.save(todo);
    }
}
