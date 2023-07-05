package my.todo.schedule.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import my.todo.member.domain.dto.UserRequestDto;
import my.todo.member.domain.user.User;
import my.todo.member.repository.UserJpaRepository;
import my.todo.schedule.domain.dto.request.ScheduleRequestDto;
import my.todo.schedule.domain.dto.request.ScheduleWithTodoRequest;
import my.todo.schedule.domain.dto.response.ScheduleResponseDto;
import my.todo.schedule.domain.dto.response.ScheduleWithTodoResponse;
import my.todo.schedule.domain.schedule.Schedule;
import my.todo.schedule.repository.CustomScheduleRepository;
//import my.todo.schedule.repository.ScheduleRepositoryImpl;
import my.todo.todo.domain.dto.TodoResponseDto;
import my.todo.todo.repository.CustomTodoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleService {

    private final CustomScheduleRepository customScheduleRepository;
    private final CustomTodoRepository customTodoRepository;
    private final UserJpaRepository userJpaRepository;

    public void add(ScheduleRequestDto scheduleRequestDto) {
        User user = userJpaRepository.getById(scheduleRequestDto.getUserId());
        Schedule schedule = scheduleRequestDto.toEntity(user);
        customScheduleRepository.save(schedule);
    }

    public List<ScheduleResponseDto> findScheduleList(UserRequestDto.LoginDTO loginDTO){
        User user = userJpaRepository.getByUsername(loginDTO.getUsername());
        return customScheduleRepository.getScheduleListByUser(user);
    }

    public ScheduleWithTodoResponse findScheduleWithTodo(ScheduleWithTodoRequest scheduleWithTodoRequest){
        Schedule schedule = scheduleWithTodoRequest.toEntity();
//        Todo: ToDo list ToDo Repository 통해서 받기
        List<TodoResponseDto> todoResponseDtoList = customTodoRepository.getTodoListBySchedule(schedule);
        return new ScheduleWithTodoResponse(schedule, todoResponseDtoList);
    }
}
