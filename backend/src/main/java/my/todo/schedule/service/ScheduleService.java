package my.todo.schedule.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import my.todo.global.error.DuplicatedException;
import my.todo.member.domain.dto.UserRequestDto;
import my.todo.member.domain.user.User;
import my.todo.member.repository.UserJpaRepository;
import my.todo.schedule.domain.dto.request.ScheduleRequestDto;
import my.todo.schedule.domain.dto.request.ScheduleUpdateRequestDto;
import my.todo.schedule.domain.dto.request.ScheduleWithTodoRequest;
import my.todo.schedule.domain.dto.response.ScheduleResponseDto;
import my.todo.schedule.domain.dto.response.ScheduleWithTodoResponse;
import my.todo.schedule.domain.schedule.Schedule;
import my.todo.schedule.repository.CustomScheduleRepository;
//import my.todo.schedule.repository.ScheduleRepositoryImpl;
import my.todo.todo.domain.dto.response.TodoResponseDto;
import my.todo.todo.repository.CustomTodoRepository;
import org.springframework.stereotype.Service;

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
        if(customScheduleRepository.existsByTitle(schedule.getTitle())){
            throw new DuplicatedException("Title Is Duplicated");
        }
        customScheduleRepository.save(schedule);
    }

    public List<ScheduleResponseDto> findScheduleList(UserRequestDto.UpdateDTO updateDTO){
        User user = userJpaRepository.getByUsername(updateDTO.getUsername());
        return customScheduleRepository.getScheduleListByUser(user);
    }

    public ScheduleWithTodoResponse findScheduleWithTodo(ScheduleWithTodoRequest scheduleWithTodoRequest){
        Schedule schedule = customScheduleRepository.getScheduleByTitle(scheduleWithTodoRequest.getTitle());
        List<TodoResponseDto> todoResponseDtoList = customTodoRepository.getTodoListBySchedule(schedule);
        return new ScheduleWithTodoResponse(schedule, todoResponseDtoList);
    }

    public void updateSchedule(ScheduleUpdateRequestDto scheduleUpdateRequestDto) {
        Schedule schedule = customScheduleRepository.getScheduleById(scheduleUpdateRequestDto.getId());
        schedule.updateTitle(scheduleUpdateRequestDto.getTitle());
        schedule.updateIsPublic(scheduleUpdateRequestDto.isPublic());
    }

    public void deleteSchedule(ScheduleUpdateRequestDto scheduleUpdateRequestDto) {
        Schedule schedule = customScheduleRepository.getScheduleById(scheduleUpdateRequestDto.getId());
        customScheduleRepository.delete(schedule);
    }
}
