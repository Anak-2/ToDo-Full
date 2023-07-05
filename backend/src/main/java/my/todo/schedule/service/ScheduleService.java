package my.todo.schedule.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import my.todo.global.error.UserNotFoundException;
import my.todo.member.domain.user.User;
import my.todo.member.repository.UserJpaRepository;
import my.todo.schedule.domain.dto.ScheduleRequestDto;
import my.todo.schedule.domain.dto.ScheduleResponseDto;
import my.todo.schedule.domain.schedule.Schedule;
import my.todo.schedule.repository.CustomScheduleRepository;
//import my.todo.schedule.repository.ScheduleRepositoryImpl;
import my.todo.todo.domain.dto.TodoRequestDto;
import my.todo.todo.domain.dto.TodoResponseDto;
import my.todo.todo.domain.todo.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleService {

    private final CustomScheduleRepository CustomScheduleRepository;
    private final UserJpaRepository userJpaRepository;

    public List<TodoResponseDto> getTodoList(Schedule schedule) {
        List<Todo> todoList = CustomScheduleRepository.getTodoListBySchedule(schedule);
        return todoList.stream()
                .map(o -> new TodoResponseDto(o))
                .collect(Collectors.toList());
    }

    public void add(ScheduleRequestDto scheduleRequestDto) {
        User user = userJpaRepository.getById(scheduleRequestDto.getUserId());
        CustomScheduleRepository.save(scheduleRequestDto.toEntity(user));
    }
}
