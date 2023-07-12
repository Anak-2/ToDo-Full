package my.todo.schedule.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import my.todo.global.error.DuplicatedException;
import my.todo.member.domain.user.User;
import my.todo.member.repository.UserJpaRepository;
import my.todo.schedule.domain.dto.request.ScheduleRequestDto;
import my.todo.schedule.domain.dto.request.ScheduleUpdateRequestDto;
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

    public ScheduleResponseDto add(User user, ScheduleRequestDto scheduleRequestDto) {
        Schedule schedule = scheduleRequestDto.toEntity(user);
        if(customScheduleRepository.existsScheduleByTitleAndUser(schedule.getTitle(), user)){
            throw new DuplicatedException("Title Is Duplicated In Same User");
        }
        customScheduleRepository.save(schedule);
        Schedule findSchedule = customScheduleRepository.getScheduleByTitleAndUser(schedule.getTitle(), user);
        return ScheduleResponseDto.builder()
                .id(findSchedule.getId())
                .title(findSchedule.getTitle())
                .createdDate(findSchedule.getCreatedDate())
                .isPublic(findSchedule.isPublic())
                .build();
    }

    public List<ScheduleResponseDto> findScheduleList(String username){
        User user = userJpaRepository.getByUsername(username);
        return customScheduleRepository.getScheduleListByUser(user);
    }

    public ScheduleWithTodoResponse findScheduleWithTodo(ScheduleRequestDto scheduleRequestDto){
        Schedule schedule = customScheduleRepository.getScheduleById(scheduleRequestDto.getId());
        List<TodoResponseDto> todoResponseDtoList = customTodoRepository.getTodoListBySchedule(schedule);
        return new ScheduleWithTodoResponse(schedule, todoResponseDtoList);
    }

    public void updateSchedule(ScheduleUpdateRequestDto scheduleUpdateRequestDto) {
        Schedule schedule = customScheduleRepository.getScheduleById(scheduleUpdateRequestDto.getId());
        schedule.updateTitle(scheduleUpdateRequestDto.getTitle());
        schedule.updateIsPublic(scheduleUpdateRequestDto.isPublic());
        System.out.println("isPublic: "+scheduleUpdateRequestDto.isPublic());
    }

    public void deleteSchedule(ScheduleUpdateRequestDto scheduleUpdateRequestDto) {
        Schedule schedule = customScheduleRepository.getScheduleById(scheduleUpdateRequestDto.getId());
        customScheduleRepository.delete(schedule);
    }
}
