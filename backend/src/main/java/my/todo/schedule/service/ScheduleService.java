package my.todo.schedule.service;

import lombok.RequiredArgsConstructor;
import my.todo.global.error.duplicatedException.DuplicatedException;
import my.todo.global.errormsg.ScheduleError;
import my.todo.member.domain.user.User;
import my.todo.member.repository.UserRepository;
import my.todo.schedule.domain.dto.request.ScheduleRequestDto;
import my.todo.schedule.domain.dto.request.ScheduleUpdateRequestDto;
import my.todo.schedule.domain.dto.response.ScheduleResponseDto;
import my.todo.schedule.domain.schedule.Schedule;
import my.todo.schedule.repository.CustomScheduleRepository;
//import my.todo.schedule.repository.ScheduleRepositoryImpl;
import my.todo.todo.repository.CustomTodoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ScheduleService {

    private final CustomScheduleRepository customScheduleRepository;
    private final CustomTodoRepository customTodoRepository;
    private final UserRepository userRepository;

    @Transactional
    public ScheduleResponseDto add(User user, ScheduleRequestDto scheduleRequestDto) {
        Schedule schedule = scheduleRequestDto.toEntity(user);
        if(customScheduleRepository.existsScheduleByTitleAndUser(schedule.getTitle(), user)){
            throw new DuplicatedException(ScheduleError.TITLE_DUPLICATED.getMsg());
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
        User user = userRepository.getByUsername(username);
        return customScheduleRepository.getScheduleListByUser(user);
    }

    @Transactional
    public void updateSchedule(ScheduleUpdateRequestDto scheduleUpdateRequestDto) {
        Schedule schedule = customScheduleRepository.getScheduleById(scheduleUpdateRequestDto.getId());
        schedule.updateTitle(scheduleUpdateRequestDto.getTitle());
        schedule.updateIsPublic(scheduleUpdateRequestDto.isPublic());
        System.out.println("isPublic: "+scheduleUpdateRequestDto.isPublic());
    }

    @Transactional
    public void deleteSchedule(ScheduleUpdateRequestDto scheduleUpdateRequestDto) {
        Schedule schedule = customScheduleRepository.getScheduleById(scheduleUpdateRequestDto.getId());
        customScheduleRepository.delete(schedule);
    }
}
