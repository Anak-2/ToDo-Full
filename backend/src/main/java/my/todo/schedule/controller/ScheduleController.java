package my.todo.schedule.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.todo.config.auth.PrincipalDetails;
import my.todo.member.domain.user.User;
import my.todo.schedule.domain.dto.request.ScheduleRequestDto;
import my.todo.schedule.domain.dto.request.ScheduleUpdateRequestDto;
import my.todo.schedule.domain.dto.response.ScheduleResponseDto;
import my.todo.schedule.domain.dto.response.ScheduleWithTodoResponse;
import my.todo.schedule.service.ScheduleService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/schedule")
@RequiredArgsConstructor
@Slf4j
public class ScheduleController {
    private final ScheduleService scheduleService;

//    add schedule
    @PostMapping("/add")
    public ScheduleResponseDto add(@RequestBody ScheduleRequestDto scheduleRequestDto, Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User user = principalDetails.getUser();
        return scheduleService.add(user, scheduleRequestDto);
    }

//    get schedule list by user id
    @GetMapping("/schedules")
    public List<ScheduleResponseDto> getScheduleList(Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User user = principalDetails.getUser();
        return scheduleService.findScheduleList(user.getUsername());
    }

//    modify schedule
    @PatchMapping("/update")
    public void updateSchedule(@RequestBody ScheduleUpdateRequestDto scheduleUpdateRequestDto){
        System.out.println(scheduleUpdateRequestDto);
        scheduleService.updateSchedule(scheduleUpdateRequestDto);
    }
//    delete schedule
    @DeleteMapping("/delete")
    public void deleteSchedule(@RequestBody ScheduleUpdateRequestDto scheduleUpdateRequestDto){
        scheduleService.deleteSchedule(scheduleUpdateRequestDto);
    }
}
