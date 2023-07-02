package my.todo.schedule.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import my.todo.schedule.domain.schedule.Schedule;
import my.todo.schedule.repository.ScheduleRepository;
import my.todo.todo.domain.dto.TodoRequestDto;
import my.todo.todo.domain.dto.TodoResponseDto;
import my.todo.todo.domain.todo.Todo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public List<TodoResponseDto> getTodoList(Schedule schedule){
        Optional<List<Todo>> todoList = scheduleRepository.findTodoListBySchedule(schedule);
        if(todoList.isPresent()){
            return todoList.get().stream()
                    .map(o-> new TodoResponseDto(o))
                    .collect(Collectors.toList());
        }
        return null;
    }
}
